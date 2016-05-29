package net.flyingfat.common.biz;

import net.flyingfat.common.biz.service.IHostService;
import net.flyingfat.common.biz.xip.SimpleBizReq;
import net.flyingfat.common.biz.xip.SimpleBizResp;
import net.flyingfat.common.serialization.protocol.annotation.BizMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class MainCourse extends BaseCourse {

  private static Logger     logger   = LoggerFactory.getLogger(MainCourse.class);
  
  @Autowired
  private IHostService hostService;

  @BizMethod
  public void onSimpleBizReq(SimpleBizReq req) {
    if (logger.isInfoEnabled()) {
      logger.info("----------->receive req, req = {}", req.toString());
    }

    String hostAddr=hostService.getHostAddrJsonStr();
    SimpleBizResp resp = new SimpleBizResp();
    resp.setHostAddr(hostAddr);
    sendBaseNormalResponse(req, resp);
    
    if (logger.isInfoEnabled()) {
    	logger.info("<----------send resp, resp = {}",resp.toString());
    }
  }

  
  
}
