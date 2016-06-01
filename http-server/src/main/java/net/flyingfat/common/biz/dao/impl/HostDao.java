package net.flyingfat.common.biz.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.flyingfat.common.biz.dao.HostMapper;
import net.flyingfat.common.biz.domain.Host;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HostDao {

	@Autowired
	private HostMapper hostMapper;
	
	public List<Host> getHost(){
		return hostMapper.getHost();
	}
	
}
