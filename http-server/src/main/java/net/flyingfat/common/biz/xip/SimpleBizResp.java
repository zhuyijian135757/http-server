package net.flyingfat.common.biz.xip;


import net.flyingfat.common.serialization.bytebean.annotation.ByteField;
import net.flyingfat.common.serialization.protocol.annotation.SignalCode;
import net.flyingfat.common.serialization.protocol.meta.MsgCode;


@SignalCode(messageCode = MsgCode.MSG_CODE_FOR_SIMPLE_BIZ_RESP)
public class SimpleBizResp extends BaseXipResponse {
	
	@ByteField(index=3, description="主机地址")
	private String hostAddr;

	public String getHostAddr() {
		return hostAddr;
	}

	public void setHostAddr(String hostAddr) {
		this.hostAddr = hostAddr;
	}
	
}
