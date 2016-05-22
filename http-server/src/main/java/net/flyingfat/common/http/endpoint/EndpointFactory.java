package net.flyingfat.common.http.endpoint;

import net.flyingfat.common.lang.Transformer;
import net.flyingfat.common.lang.holder.Holder;
import net.flyingfat.common.lang.transport.Receiver;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;

public interface EndpointFactory {
	
	public abstract Endpoint createEndpoint(ChannelHandlerContext ctx,
			Transformer<Object, FullHttpResponse> paramTransformer);

	public abstract void setMessageClosure(Receiver paramReceiver);

}
