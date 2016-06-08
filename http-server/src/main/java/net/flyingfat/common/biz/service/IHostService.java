package net.flyingfat.common.biz.service;

import org.springframework.cache.annotation.Cacheable;


public interface IHostService {

	public String getHostAddrJsonStr();
	
}
