package net.flyingfat.common.http.reactor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

public interface HttpReactor {
	public abstract void onHttpRequest(ChannelHandlerContext ctx,
			HttpRequest paramHttpRequest);
}
