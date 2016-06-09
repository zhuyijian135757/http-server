package net.flyingfat.common.biz.dao.impl;

import java.util.List;

import net.flyingfat.common.biz.aop.CacheAno;
import net.flyingfat.common.biz.dao.HostMapper;
import net.flyingfat.common.biz.domain.Host;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Component
public class HostDao   {

	@Autowired
	private HostMapper hostMapper;
	
	private final static String CACHE_NAME="HOST";
	
	@Cacheable(value=CACHE_NAME)
	public List<Host> getHost(){
		return hostMapper.getHost();
	}
	
}
