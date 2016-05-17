package net.flyingfat.common.http.response;

import java.io.UnsupportedEncodingException;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public interface HttpResponseSender {
	public abstract void sendResponse(ChannelHandlerContext ctx,
			HttpResponse paramHttpResponse);

	public abstract void sendResponse(ChannelHandlerContext ctx,
			HttpResponseStatus paramHttpResponseStatus, String paramString);

	public abstract void sendResponse(ChannelHandlerContext ctx,
			HttpResponseStatus paramHttpResponseStatus, String paramString1,
			String paramString2) throws UnsupportedEncodingException;

	public abstract void sendRedirectResponse(ChannelHandlerContext ctx,
			String paramString);

	public abstract String sendFile(ChannelHandlerContext ctx,
			byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}
