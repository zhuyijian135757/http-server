package net.flyingfat.common.biz.aop;

import java.util.List;

import net.flyingfat.common.biz.domain.Host;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CacheAop {

	@Pointcut("execution(* net.flyingfat.common.biz.service.IHostService.getHostAddrJsonStr())")
	public void aop(){};
	
	@Before("aop()")
    public void before() {
        System.out.println("before aop----------------------");
    }
	
	/*@Around("cacheAop()")
	public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
		return pjp.proceed(null);
	}*/
	
    /*@After("aop()")
    public void after() {
        System.out.println("method after");
    }*/
    
   /* @org.aspectj.lang.annotation.AfterReturning("execution(public * net.flyingfat.common.biz.dao.impl.HostDao.*(..))")
    public void AfterReturning() {
        System.out.println("method AfterReturning");
    }
    
    @org.aspectj.lang.annotation.AfterThrowing("execution(public * net.flyingfat.common.biz.dao.impl.HostDao.*(..))")
    public void AfterThrowing() {
        System.out.println("method AfterThrowing");
    }*/ 
	
}
