package net.flyingfat.common.http.endpoint;

import net.flyingfat.common.lang.Transformer;
import net.flyingfat.common.lang.transport.Receiver;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;

public class DefaultEndpointFactory implements EndpointFactory {
	private Receiver messageClosure = null;

	public Endpoint createEndpoint(ChannelHandlerContext ctx,
			Transformer<Object, FullHttpResponse> responseEncoder) {
		ServerEndpoint endpoint = new ServerEndpoint();
		endpoint.setCtx(ctx);
		endpoint.setMessageClosure(messageClosure);
		endpoint.setResponseEncoder(responseEncoder);
		endpoint.start();

		return endpoint;
	}

	public void setMessageClosure(Receiver messageClosure) {
		this.messageClosure = messageClosure;
	}


}
