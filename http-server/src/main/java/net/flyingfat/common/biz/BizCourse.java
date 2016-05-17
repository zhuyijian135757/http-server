package net.flyingfat.common.biz;

import net.flyingfat.common.biz.xip.BaseXipRequest;
import net.flyingfat.common.biz.xip.BaseXipResponse;

public interface BizCourse {

	void sendBaseNormalResponse(BaseXipRequest req, BaseXipResponse resp);
	
}
