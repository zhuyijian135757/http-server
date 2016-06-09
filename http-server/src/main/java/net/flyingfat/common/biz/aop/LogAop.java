package net.flyingfat.common.biz.aop;

import net.flyingfat.common.biz.MainCourse;
import net.flyingfat.common.serialization.protocol.annotation.BizMethod;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAop {
	
	private Logger logger = LoggerFactory.getLogger(MainCourse.class);
	
	@Around(value="@annotation(logAno) && @annotation(bizMethod)")
	public Object interceptor(ProceedingJoinPoint pjp,LogAno logAno,BizMethod bizMethod) throws Throwable {
		if (logger.isInfoEnabled()) {
			logger.info("----------->receive req, req = {}", pjp.getArgs());
		}
		return pjp.proceed();
	}
}
