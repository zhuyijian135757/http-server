
package net.flyingfat.common.http;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import net.flyingfat.common.booter.Booter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleStateHandler;


public final class HttpServer implements SmartInitializingSingleton  {

	private  final Logger logger = LoggerFactory.getLogger(Booter.class);
	private  Boolean ssl;
	private  String  ip;
    private  Integer  port;
    private  HttpServerHandler httpServerHandler;

    public void afterSingletonsInstantiated() {
    	final SslContext sslCtx=getSSLContext();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 10240)
             .option(ChannelOption.SO_REUSEADDR, true)
             .option(ChannelOption.SO_KEEPALIVE, true)
             .option(ChannelOption.TCP_NODELAY, true)
             .option(ChannelOption.SO_LINGER, -1)
             .option(ChannelOption.SO_SNDBUF, -1)
             .option(ChannelOption.SO_RCVBUF, 1024);
             
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>(){
            	    @Override
            	    public void initChannel(SocketChannel ch) {
            	        ChannelPipeline p = ch.pipeline();
            	        if (getSSLContext() != null) {
            	            p.addLast(sslCtx.newHandler(ch.alloc()));
            	        }
            	        p.addLast(new HttpServerCodec());
            	        p.addLast(new HttpObjectAggregator(1048576));
            	        p.addLast(new IdleStateHandler(10, 10 , 10));
            	        p.addLast(httpServerHandler);
            	    }
             });
             b.bind(ip,port).sync();
        }catch(Exception e){
        	logger.error("start HttpServer error {} ",e.getMessage());
        }
    }
    
    public SslContext getSSLContext(){
        SslContext sslCtx = null;;
        if (ssl) {
            try {
            	SelfSignedCertificate ssc = new SelfSignedCertificate();
				sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
			} catch (SSLException e) {
				e.printStackTrace();
			}catch (CertificateException e){
				e.printStackTrace();
			}
        }
        return sslCtx;
    }

	public HttpServerHandler getHttpServerHandler() {
		return httpServerHandler;
	}

	public void setHttpServerHandler(HttpServerHandler httpServerHandler) {
		this.httpServerHandler = httpServerHandler;
	}

	public void setSsl(Boolean ssl) {
		this.ssl = ssl;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}
