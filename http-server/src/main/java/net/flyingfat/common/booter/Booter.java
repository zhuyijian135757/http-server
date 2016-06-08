package net.flyingfat.common.booter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Booter {
	
	private static final Logger logger = LoggerFactory.getLogger(Booter.class);
	
	public static void main(String[] args) {
		try {
			new ClassPathXmlApplicationContext("spring/applicationContext.xml",
					 "spring/applicationContext-persistence.xml");
		} catch (Exception e) {
			logger.error("{}",e);
		}
	}

} 
