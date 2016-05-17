package net.flyingfat.common.biz.xip;


import net.flyingfat.common.serialization.protocol.annotation.SignalCode;
import net.flyingfat.common.serialization.protocol.meta.MessageCode;


@SignalCode(messageCode = MessageCode.MSG_CODE_FOR_SIMPLE_BIZ_RESP)
public class SimpleBizResp extends BaseXipResponse {
}
