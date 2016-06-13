package net.flyingfat.common.http.endpoint;

import java.net.InetSocketAddress;
import net.flyingfat.common.http.TransportUtil;
import net.flyingfat.common.http.codec.HttpResponseEncoder;
import net.flyingfat.common.http.response.ConstantResponse;
import net.flyingfat.common.http.response.DefaultHttpResponseSender;
import net.flyingfat.common.http.response.HttpResponseSender;
import net.flyingfat.common.lang.IpPortPair;
import net.flyingfat.common.lang.KeyTransformer;
import net.flyingfat.common.lang.Transformer;
import net.flyingfat.common.lang.transport.Receiver;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerEndpoint implements Endpoint {
	private static final Logger logger = LoggerFactory
			.getLogger(ServerEndpoint.class);
	private Receiver messageClosure = null;
	private ChannelHandlerContext ctx = null;
	private HttpResponseSender httpResponseSender = new DefaultHttpResponseSender();
	private Transformer<Object, FullHttpResponse> responseEncoder = new HttpResponseEncoder();
	private InetSocketAddress addr = null;
	private HttpRequest req;

	public void send(Object bean) {
		if (null != bean) {
			HttpRequest req = getReq();
			if (req == null) {
				logger.warn("cannot get HttpRequest");
				doExceptionSend();
				return;
			}
			TransportUtil.attachRequest(bean, req);
			doSend(bean);
		}
	}

	public void send(Object bean, Receiver receiver) {
		throw new UnsupportedOperationException("not implemented yet!");
	}

	public void messageReceived(Object msg) {
		setReq(TransportUtil.getRequestOf(msg));
		if (messageClosure != null) {
			messageClosure.messageReceived(msg);
		}
	}

	public void stop() {
		this.messageClosure = null;
		this.ctx = null;
	}

	public void start() {
	}

	private void doSend(Object bean) {
		if (bean != null) {
			HttpResponse response = (HttpResponse) responseEncoder
					.transform(bean);
			httpResponseSender.sendResponse(ctx, response);
		}
	}

	private void doExceptionSend() {
		HttpResponse response = ConstantResponse.getResponseServerBusy();
		httpResponseSender.sendResponse(ctx, response);
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public void setResponseEncoder(
			Transformer<Object, FullHttpResponse> responseEncoder) {
		this.responseEncoder = responseEncoder;
	}

	public void setMessageClosure(Receiver messageClosure) {
		this.messageClosure = messageClosure;
	}

	public IpPortPair getRemoteAddress() {
		if (this.addr != null) {
			return new IpPortPair(this.addr.getAddress().getHostAddress(),
					this.addr.getPort());
		}
		InetSocketAddress addr = (InetSocketAddress) ctx.channel()
				.remoteAddress();
		return new IpPortPair(addr.getAddress().getHostAddress(),
				addr.getPort());
	}

	public void setAddr(InetSocketAddress addr) {
		this.addr = addr;
	}

	public HttpRequest getReq() {
		return req;
	}

	public void setReq(HttpRequest req) {
		this.req = req;
	}

}
