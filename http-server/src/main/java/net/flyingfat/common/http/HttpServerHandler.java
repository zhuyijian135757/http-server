package net.flyingfat.common.http;

import java.io.IOException;
import java.net.InetSocketAddress;

import net.flyingfat.common.http.endpoint.DefaultEndpointFactory;
import net.flyingfat.common.http.endpoint.Endpoint;
import net.flyingfat.common.http.endpoint.EndpointFactory;
import net.flyingfat.common.http.reactor.ConstantResponseReactor;
import net.flyingfat.common.http.reactor.HttpReactor;
import net.flyingfat.common.http.response.ConstantResponse;
import net.flyingfat.common.lang.Transformer;
import net.flyingfat.common.lang.holder.Holder;
import net.flyingfat.common.lang.transport.Receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.channel.ChannelHandler.Sharable;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

@Sharable
public class HttpServerHandler extends
		SimpleChannelInboundHandler<FullHttpRequest> {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpServerHandler.class);
	private Transformer<FullHttpRequest, Object> requestDecoder = null;
	private Transformer<Object, FullHttpResponse> responseEncoder = null;
	private HttpReactor errorReactor = new ConstantResponseReactor(
			ConstantResponse.get400NobodyResponse());
	private EndpointFactory endpointFactory = new DefaultEndpointFactory();

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			FullHttpRequest request) {
		if (logger.isDebugEnabled()) {
			logger.trace("message received {}", request);
		}
		if (HttpHeaders.is100ContinueExpected(request)) {
			ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
		}

		Object signal = requestDecoder.transform(request);
		if (null != signal) {
			Endpoint endpoint = TransportUtil.getEndpointOfCtx(ctx);
			if (null != endpoint) {
				TransportUtil.attachSender(signal, endpoint);
				TransportUtil.attachRequest(signal, request);
				endpoint.messageReceived(signal);
			} else {
				logger.warn("missing endpoint, ignore incoming msg:", signal);
			}
		} else if (null != errorReactor) {
			logger.error("content is null, try send back client empty HttpResponse.");
			errorReactor.onHttpRequest(ctx, request);
		} else {
			logger.warn(
					"Can not transform bean for req {}, and missing errorHandler.",
					request);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("channelOpen: {}", ctx.channel());
		}
		Endpoint endpoint = endpointFactory
				.createEndpoint(ctx, responseEncoder);
		if (null != endpoint) {
			TransportUtil.attachEndpointToCtx(ctx, endpoint);
			endpoint.setAddr((InetSocketAddress) ctx.channel().remoteAddress());
		}
	};

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("channelClosed: {}", ctx.channel());
		}
		Endpoint endpoint = TransportUtil.getEndpointOfCtx(ctx);
		if (null != endpoint) {
			endpoint.stop();
		}
		TransportUtil.detachEndpointToCtx(ctx);
	};

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		if (!(cause.getCause() instanceof IOException)) {
			logger.error("exceptionCaught: {} ", cause.getCause());
		}
		ctx.channel().close();
	}

	public Transformer<FullHttpRequest, Object> getRequestDecoder() {
		return requestDecoder;
	}

	public void setRequestDecoder(
			Transformer<FullHttpRequest, Object> requestDecoder) {
		this.requestDecoder = requestDecoder;
	}

	public Transformer<Object, FullHttpResponse> getResponseEncoder() {
		return responseEncoder;
	}

	public void setResponseEncoder(
			Transformer<Object, FullHttpResponse> responseEncoder) {
		this.responseEncoder = responseEncoder;
	}

	public void setMessageClosure(Receiver messageClosure) {
		this.endpointFactory.setMessageClosure(messageClosure);
	}

	public void setResponseContext(Holder responseContext) {
		this.endpointFactory.setResponseContext(responseContext);
	}

	public void setEndpointFactory(EndpointFactory endpointFactory) {
		this.endpointFactory = endpointFactory;
	}

}
