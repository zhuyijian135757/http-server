package net.flyingfat.common.biz;

import net.flyingfat.common.biz.xip.SimpleBizReq;
import net.flyingfat.common.biz.xip.SimpleBizResp;
import net.flyingfat.common.serialization.protocol.annotation.BizMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainCourse extends BaseCourse {

  private static Logger     logger   = LoggerFactory.getLogger(MainCourse.class);

  @BizMethod
  public void onSimpleBizReq(SimpleBizReq req) {
    if (logger.isInfoEnabled()) {
      logger.info("----------->receive req, req = {}", req.toString());
    }

    SimpleBizResp resp = new SimpleBizResp();
    sendBaseNormalResponse(req, resp);
    
    if (logger.isInfoEnabled()) {
    	logger.info("<----------send resp, resp = {}",resp.toString());
    }
  }

  
  
}
