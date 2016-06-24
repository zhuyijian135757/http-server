package net.flyingfat.http;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;
import net.flyingfat.common.biz.xip.BaseXipRequest;
import net.flyingfat.common.biz.xip.SimpleBizReq;
import net.flyingfat.common.biz.xip.SimpleBizResp;
import net.flyingfat.common.http.codec.HttpRequestEncoder;
import net.flyingfat.common.http.codec.HttpResponseDecoder;
import net.flyingfat.common.jmx.ManagedInterfaces;
import net.flyingfat.common.serialization.protocol.xip.XipSignal;

public class HttpClientCase extends TestCase {

	private HttpRequestEncoder encoder;
	private HttpResponseDecoder decoder;
	
	@Before
	public void setUp(){
		  ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:applicationContext-test.xml");
		  encoder=(HttpRequestEncoder) applicationContext.getBean("httpRequestEncoder");
		  decoder=(HttpResponseDecoder) applicationContext.getBean("httpResponseDecoder");
	}

	@Test
	public void testSimpleBizReq() throws Exception {
		SimpleBizReq req=new SimpleBizReq();
		req.setUid("ddd");
		req.setId(1l);
		req.setOrderId(10);
		SimpleBizResp resp = (SimpleBizResp) send(req);
		System.out.println(resp.toString());
		
		/*ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:applicationContext-test.xml");
		ManagedInterfaces managedInterfaces=(ManagedInterfaces) applicationContext.getBean("proxy");
		managedInterfaces.removeAll();*/
		
		/*SSLContext sslcontext = SSLContexts.createSystemDefault();
		SocketFactory sf = sslcontext.getSocketFactory();
		SSLSocket socket = (SSLSocket) sf.createSocket("somehost", 443);
		socket.setEnabledProtocols(new String[] {"TLSv1","TLSv1.1","TLSv1.2"});
		socket.setEnabledCipherSuites(new String[] {"TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA, TLS_RSA_WITH_AES_128_CBC_SHA, SSL_RSA_WITH_3DES_EDE_CBC_SHA"});*/
		
	}

	public XipSignal send(BaseXipRequest req) {
		
		InputStream input = null;
		ByteArrayOutputStream out = null;
		HttpClient client = new DefaultHttpClient();
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1)
						throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1)
						throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}  
			};  
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx,NoopHostnameVerifier.INSTANCE);
			client =HttpClients.custom().setSSLSocketFactory(ssf).build();
			
			HttpPost post = new HttpPost("https://localhost:7903");
			post.setHeader(
					"User-Agent",
					"Mozilla/5.0 (Linux; U; Android 2.2.1; en-us; Nexus One Build/FRG83) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
			post.setHeader("Expect", "100-continue");
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
			XipSignal signal = decoder.decodeXipSignal(out.toByteArray());
			return signal;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				//Thread.sleep(10000);
				out.close();
				input.close();
				client.getConnectionManager().shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	

}
