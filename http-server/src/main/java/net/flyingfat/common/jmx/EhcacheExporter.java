package net.flyingfat.common.jmx;

import javax.management.MBeanServer;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EhcacheExporter implements InitializingBean {
	
	@Autowired
	private CacheManager ehcache;
	
	@Autowired
	private MBeanServer  mbeanServer;

	@Override
	public void afterPropertiesSet() throws Exception {
		ManagementService.registerMBeans(ehcache, mbeanServer, true, true, true, true);
	}
	
	
}
