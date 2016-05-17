package net.flyingfat.common.http.response;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class ConstantResponse {
	public static final DefaultHttpResponse RESPONSE_200_NOBODY;
	public static final DefaultHttpResponse RESPONSE_400_NOBODY;
	public static final DefaultHttpResponse RESPONSE_SERVER_BUSY;

	static {
		RESPONSE_200_NOBODY = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.OK, Unpooled.wrappedBuffer("OK".getBytes()));
		RESPONSE_400_NOBODY = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.BAD_REQUEST);
		RESPONSE_SERVER_BUSY = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, HttpResponseStatus.SERVICE_UNAVAILABLE,
				Unpooled.wrappedBuffer("Server Too Busy".getBytes()));
	}

	public static final DefaultHttpResponse RESPONSE_404_NOT_FOUND = new DefaultHttpResponse(
			HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
	public static final DefaultHttpResponse RESPONSE_GATEWAY_TIMEOUT = new DefaultHttpResponse(
			HttpVersion.HTTP_1_1, HttpResponseStatus.GATEWAY_TIMEOUT);

	public static HttpResponse get200NobodyResponse() {
		return RESPONSE_200_NOBODY;
	}

	public static HttpResponse get400NobodyResponse() {
		return RESPONSE_400_NOBODY;
	}

	public static HttpResponse getResponseServerBusy() {
		return RESPONSE_SERVER_BUSY;
	}

	public static HttpResponse get404NotFoundResponse() {
		return RESPONSE_404_NOT_FOUND;
	}

	public static HttpResponse getGatewayTimeoutResponse() {
		return RESPONSE_GATEWAY_TIMEOUT;
	}

	public static HttpResponse get200WithContentTypeResponse(String contentType) {
		DefaultHttpResponse response200WithContentType = new DefaultHttpResponse(
				HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		response200WithContentType.headers().add("Content-Type", contentType);
		return response200WithContentType;
	}
}
