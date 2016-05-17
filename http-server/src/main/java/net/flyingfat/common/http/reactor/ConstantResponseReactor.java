package net.flyingfat.common.http.reactor;

import net.flyingfat.common.http.response.DefaultHttpResponseSender;
import net.flyingfat.common.http.response.HttpResponseSender;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class ConstantResponseReactor implements HttpReactor {
	private HttpReactor nextReactor = null;
	private HttpResponse response;
	private HttpResponseSender responseSender = new DefaultHttpResponseSender();

	public ConstantResponseReactor(HttpResponse response) {
		this.response = response;
	}

	public HttpResponse getResponse() {
		return this.response;
	}

	public HttpResponseSender getResponseSender() {
		return this.responseSender;
	}

	public void setResponseSender(HttpResponseSender responseSender) {
		this.responseSender = responseSender;
	}

	public HttpReactor getNextReactor() {
		return this.nextReactor;
	}

	public void setNextReactor(HttpReactor nextReactor) {
		this.nextReactor = nextReactor;
	}

	public void onHttpRequest(ChannelHandlerContext ctx, HttpRequest request) {
		String uuid = request.headers().get("uuid");
		if (uuid != null) {
			this.response.headers().set("uuid", uuid);
		}
		this.responseSender.sendResponse(ctx, this.response);
		if (null != this.nextReactor) {
			this.nextReactor.onHttpRequest(null, request);
		}
	}

}
