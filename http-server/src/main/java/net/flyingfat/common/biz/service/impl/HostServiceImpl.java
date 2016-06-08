package net.flyingfat.common.biz.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.flyingfat.common.biz.dao.impl.HostDao;
import net.flyingfat.common.biz.domain.Host;
import net.flyingfat.common.biz.service.IHostService;

import com.google.gson.Gson;

@Service("hostService")
public class HostServiceImpl implements IHostService   {

	@Autowired
	private HostDao hostDao;

	public String getHostAddrJsonStr() {
		List<Host> hosts=hostDao.getHost();
		Gson gson=new Gson();
		Map<String,Set<String>> map=new HashMap<String,Set<String>>(); 
		for(Host addr : hosts){
			if(map.containsKey(addr.getHostType())){
				map.get(addr.getHostType()).add(addr.getHostAddr());
			}else {
				Set<String> set=new HashSet<String>();
				set.add(addr.getHostAddr());
				map.put(addr.getHostType(),set);
			}
		}
		return gson.toJson(map);
	}
	
	


}
