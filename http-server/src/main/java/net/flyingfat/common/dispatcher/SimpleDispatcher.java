package net.flyingfat.common.dispatcher;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.flyingfat.common.biz.BizCourse;
import net.flyingfat.common.lang.ClassUtil;
import net.flyingfat.common.lang.SimpleCache;
import net.flyingfat.common.lang.statistic.TransactionStatisticer;
import net.flyingfat.common.lang.transport.Receiver;
import net.flyingfat.common.serialization.protocol.annotation.BizMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleDispatcher implements Receiver {
	private static final Logger logger = LoggerFactory
			.getLogger(SimpleDispatcher.class);
	private Map<Class<?>, BizCourse> courseTable = new HashMap<Class<?>, BizCourse>();
	private ExecutorService mainExecutor = Executors.newSingleThreadExecutor();
	private TransactionStatisticer statisticer = new TransactionStatisticer();
	private static final Method EMPTY_METHOD;

	private static final class Key {
		private Class<?> courseClass;
		private Class<?> beanClass;

		public Key(Class<?> courseClass, Class<?> beanClass) {
			this.courseClass = courseClass;
			this.beanClass = beanClass;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((beanClass == null) ? 0 : beanClass.hashCode());
			result = prime * result
					+ ((courseClass == null) ? 0 : courseClass.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (beanClass == null) {
				if (other.beanClass != null)
					return false;
			} else if (!beanClass.equals(other.beanClass))
				return false;
			if (courseClass == null) {
				if (other.courseClass != null)
					return false;
			} else if (!courseClass.equals(other.courseClass))
				return false;
			return true;
		}
		
	}

	static {
		Method tmp = null;
		try {
			tmp = Key.class.getMethod("hashCode", new Class[0]);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		EMPTY_METHOD = tmp;
	}

	private SimpleCache<Key, Method> bizMethodCache = new SimpleCache<Key, Method>();

	private Method getBizMethod(final Class<?> courseClass,
			final Class<?> beanClass) {
		Method ret = (Method) bizMethodCache.get(
				new Key(courseClass, beanClass), new Callable<Method>() {
					public Method call() throws Exception {
						Method[] methods = ClassUtil
								.getAllMethodOf(courseClass);
						for (Method method : methods) {
							BizMethod biz = (BizMethod) method
									.getAnnotation(BizMethod.class);
							if (null != biz) {
								Class<?>[] params = method.getParameterTypes();
								if (params.length < 1) {
									if (logger.isWarnEnabled()) {
									    logger.warn("Method {} found, but {} parameters found , need to be 1.",new Object[]{method.getName(),
											   params.length});  
									}
								} else if (params[0].isAssignableFrom(beanClass)) {
									return method;
								}
							}
						}
						return SimpleDispatcher.EMPTY_METHOD;
					}
				});
		return ret == EMPTY_METHOD ? null : ret;
	}

	public void messageReceived(final Object input) {
		Runnable task = new Runnable() {
			public void run() {
				Object message = input;

				BizCourse course = getCourse(message.getClass());
				if (course == null) {
					if (logger.isErrorEnabled()) {
						logger.error("No course class found for {}. Process stopped.",message.getClass().getName());
					}
					return;
				}
				try {
					if (statisticer != null) {
						statisticer.incHandledTransactionStart();
					}
					invokeBizMethod(course, message);
					if (statisticer != null) {
						statisticer.incHandledTransactionEnd();
					}
				} catch (Exception e) {
					logger.error("biz error {}.", e);
				}
			}
		};
		if (mainExecutor != null) {
			mainExecutor.submit(task);
		} else {
			task.run();
		}
	}

	private void invokeBizMethod(BizCourse course, Object msg) {
		Method bizMethod = getBizMethod(course.getClass(), msg.getClass());
		if (null != bizMethod) {
			try {
				bizMethod.invoke(course, new Object[] { msg });
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error("Invoke biz method {} failed, error info {}", new Object[]{bizMethod.getName(), e});
				}
			}
		} else if (logger.isErrorEnabled()) {
			logger.error("No biz method found for message {}. No process execute.",msg.getClass().getName());
		}
	}

	private BizCourse getCourse(Class<?> clazz) {
		return (BizCourse) courseTable.get(clazz);
	}

	public void setCourses(Collection<BizCourse> courses) {
		for (BizCourse course : courses) {
			Method[] methods = ClassUtil.getAllMethodOf(course.getClass());
			for (Method method : methods) {
				BizMethod biz = (BizMethod) method
						.getAnnotation(BizMethod.class);
				if (null != biz) {
					Class<?>[] params = method.getParameterTypes();
					if (params.length >= 1) {
						courseTable.put(params[0], course);
					}
				}
			}
		}
	}

	public void setThreads(int threads) {
		this.mainExecutor = Executors.newFixedThreadPool(threads);
	}

	public void setStatisticer(TransactionStatisticer statisticer) {
		this.statisticer = statisticer;
	}
}
