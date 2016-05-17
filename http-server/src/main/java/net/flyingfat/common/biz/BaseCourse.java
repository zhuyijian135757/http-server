package net.flyingfat.common.biz;

import net.flyingfat.common.biz.xip.BaseXipRequest;
import net.flyingfat.common.biz.xip.BaseXipResponse;
import net.flyingfat.common.http.TransportUtil;
import net.flyingfat.common.lang.transport.Sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BaseCourse implements BizCourse {

  private Logger    logger = LoggerFactory.getLogger(BaseCourse.class);

  public void sendBaseNormalResponse(BaseXipRequest req, BaseXipResponse resp) {
    resp.setIdentification(req.getIdentification());
    Sender sender = TransportUtil.getSenderOf(req);
    sender.send(resp);
  }



}
