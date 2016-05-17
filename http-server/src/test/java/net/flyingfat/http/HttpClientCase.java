package net.flyingfat.http;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import junit.framework.TestCase;
import net.flyingfat.common.biz.xip.BaseXipRequest;
import net.flyingfat.common.biz.xip.SimpleBizReq;
import net.flyingfat.common.biz.xip.SimpleBizResp;
import net.flyingfat.common.http.codec.HttpRequestEncoder;
import net.flyingfat.common.http.codec.HttpResponseDecoder;
import net.flyingfat.common.serialization.protocol.meta.DefaultMsgCode2TypeMetainfo;
import net.flyingfat.common.serialization.protocol.meta.MetainfoUtils;
import net.flyingfat.common.serialization.protocol.xip.XipSignal;

public class HttpClientCase extends TestCase {

	static HttpRequestEncoder encoder=new HttpRequestEncoder();
	static HttpResponseDecoder decoder=new HttpResponseDecoder();

	@Test
	public void testSimpleBizReq() {
		
		  List<String> pkgs=new ArrayList<String>();
		  pkgs.add("net.flyingfat.common.biz.xip.*");
		  DefaultMsgCode2TypeMetainfo typeMetaInfo=MetainfoUtils.createTypeMetainfo(pkgs);
		
		   encoder.setDebugEnabled(true);
		   encoder.setEncryptKey("__jDlog_".getBytes());
		   encoder.setReserved(1);
		 		
		   decoder.setDebugEnabled(true);
		   decoder.setEncryptKey("__jDlog_".getBytes());
		   decoder.setTypeMetaInfo(typeMetaInfo);

		  SimpleBizReq req=new SimpleBizReq();
		  req.setUid("ddd");
		  req.setId(1l);
		  req.setOrderId(10);
		  
		  SimpleBizResp resp = (SimpleBizResp) send(req);
		  System.out.println(resp.toString());
		
	}

	public static XipSignal send(BaseXipRequest req) {
		InputStream input = null;
		ByteArrayOutputStream out = null;
		HttpClient client = new DefaultHttpClient();
		
		try {
			HttpPost post = new HttpPost("http://127.0.0.1:8080");
			post.setHeader(
					"User-Agent",
					"Mozilla/5.0 (Linux; U; Android 2.2.1; en-us; Nexus One Build/FRG83) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
			post.setHeader("Expect", "100-continue");
			//post.addHeader("Connection", "keep-alive");
			post.setHeader("clientIp", "1.1.1.1");
			byte[] body=encoder.encodeXip(req);
			HttpEntity entity=new ByteArrayEntity(body);
			post.setEntity(entity);
			HttpResponse resp = client.execute(post);
			HttpEntity content = resp.getEntity();
			input = content.getContent();
			out = new ByteArrayOutputStream();
			byte by[] = new byte[1024];
			int len = 0;
			while ((len = input.read(by)) != -1) {
				out.write(by, 0, len);
			}
			//System.out.println(new String(out.toByteArray()));
			XipSignal signal = decoder.decodeXipSignal(out.toByteArray());
			return signal;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				out.close();
				input.close();
				client.getConnectionManager().shutdown();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
