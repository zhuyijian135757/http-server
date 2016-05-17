package net.flyingfat.common.http.response;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultHttpResponseSender implements HttpResponseSender {
	private static final Logger logger = LoggerFactory
			.getLogger(DefaultHttpResponseSender.class);

	public void sendResponse(ChannelHandlerContext ctx, HttpResponse response) {
		if (ctx == null) {
			logger.warn(
					"send response, but the channel is closed, responseName=[{}]",
					response.getClass());
			return;
		}
		ChannelFuture future = ctx.writeAndFlush(response);
		if ((!HttpHeaders.isKeepAlive(response))
				|| (!response.headers().contains("Content-Length"))) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	public void sendResponse(ChannelHandlerContext ctx,
			HttpResponseStatus httpResponseStatus, String responseContent) {
		try {
			sendResponse(ctx, httpResponseStatus, responseContent, "UTF-8");
		} catch (UnsupportedEncodingException ignore) {
		}
	}

	public void sendResponse(ChannelHandlerContext ctx,
			HttpResponseStatus httpResponseStatus, String responseContent,
			String charsetName) throws UnsupportedEncodingException {
		byte[] contents = responseContent.getBytes(charsetName);
		DefaultFullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, httpResponseStatus,
				Unpooled.wrappedBuffer(contents));
		response.headers().set("Content-Length",
				Integer.valueOf(contents.length));
		sendResponse(ctx, response);
	}

	public void sendRedirectResponse(ChannelHandlerContext ctx,
			String redirectUrl) {
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.TEMPORARY_REDIRECT);
		response.headers().set("Location", redirectUrl);
		sendResponse(ctx, response);
	}

	public String sendFile(ChannelHandlerContext ctx, byte[] fullContent,
			int startPos, int endPos) {
		HttpResponseStatus httpResponseStatus = (startPos > 0) || (endPos > 0) ? HttpResponseStatus.PARTIAL_CONTENT
				: HttpResponseStatus.OK;
		if ((startPos < 0) || (startPos > fullContent.length)) {
			startPos = 0;
		}
		if ((endPos < startPos) || (endPos > fullContent.length)
				|| (endPos <= 0)) {
			endPos = fullContent.length;
		}
		byte[] partialContent = Arrays.copyOfRange(fullContent, startPos,
				endPos + 1);
		HttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, httpResponseStatus,
				Unpooled.wrappedBuffer(partialContent));
		response.headers().set("Content-Length",
				Integer.valueOf(partialContent.length));
		String range = "bytes " + startPos + "-" + endPos + "/"
				+ fullContent.length;
		response.headers().set("Content-Range", range);
		sendResponse(ctx, response);

		return httpResponseStatus.equals(HttpResponseStatus.PARTIAL_CONTENT) ? range
				: null;
	}

}
