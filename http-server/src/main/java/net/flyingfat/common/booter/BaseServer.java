package net.flyingfat.common.booter;

import java.util.ArrayList;
import java.util.List;

import net.flyingfat.common.biz.BizCourse;
import net.flyingfat.common.biz.MainCourse;
import net.flyingfat.common.dispatcher.SimpleDispatcher;
import net.flyingfat.common.http.HttpServer;
import net.flyingfat.common.http.HttpServerHandler;
import net.flyingfat.common.http.codec.HttpRequestDecoder;
import net.flyingfat.common.http.codec.HttpResponseEncoder;
import net.flyingfat.common.serialization.protocol.meta.DefaultMsgCode2TypeMetainfo;
import net.flyingfat.common.serialization.protocol.meta.MetainfoUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseServer {
	
	private static final Logger logger = LoggerFactory.getLogger(BaseServer.class);
	
	private static final HttpServerHandler httpServerHandler=new HttpServerHandler();
	
	public static void main(String[] args) {
		
		try {
			
			List<String> pkgs=new ArrayList<String>();
			pkgs.add("net.flyingfat.common.biz.xip.*");
			DefaultMsgCode2TypeMetainfo typeMetaInfo=MetainfoUtils.createTypeMetainfo(pkgs);
			
			HttpRequestDecoder reqDecoder=new HttpRequestDecoder();
			reqDecoder.setDebugEnabled(true);
			reqDecoder.setEncryptKey("__jDlog_".getBytes());
			reqDecoder.setTypeMetaInfo(typeMetaInfo);
			
			HttpResponseEncoder respEncoder=new HttpResponseEncoder();
			respEncoder.setDebugEnabled(true);
			respEncoder.setEncryptKey("__jDlog_".getBytes());
			
			SimpleDispatcher disPatcher=new SimpleDispatcher();
			disPatcher.setThreads(3);
			List<BizCourse> bCourse=new ArrayList<BizCourse>();
			bCourse.add(new MainCourse());
			disPatcher.setCourses(bCourse);
			
			HttpServer httpServer=new HttpServer();
			//httpServer.setAcceptIp("0.0.0.0");
			//httpServer.setAcceptPort(8088);
			//httpServer.setIdleTime(300);
			httpServerHandler.setRequestDecoder(reqDecoder);
			httpServerHandler.setResponseEncoder(respEncoder);
			httpServerHandler.setMessageClosure(disPatcher);
			httpServer.setHttpServerHandler(httpServerHandler);
			httpServer.start();
			
			System.out.println("httpServer started");
			
		} catch (Exception e) {
			logger.error("{}",e);
		}
		
	}

}
