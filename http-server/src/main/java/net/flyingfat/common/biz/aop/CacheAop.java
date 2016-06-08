package net.flyingfat.common.biz.aop;

import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CacheAop {
	@Around(value="@annotation(jcache)")
	public Object interceptor(ProceedingJoinPoint pjp,Jcache jcache) throws Throwable {
		return pjp.proceed();
	}
}
