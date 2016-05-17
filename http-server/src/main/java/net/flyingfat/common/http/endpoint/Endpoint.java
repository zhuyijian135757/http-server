package net.flyingfat.common.http.endpoint;

import java.net.InetSocketAddress;

import net.flyingfat.common.lang.IpPortPair;
import net.flyingfat.common.lang.transport.Receiver;
import net.flyingfat.common.lang.transport.Sender;
import io.netty.channel.ChannelHandlerContext;

public interface Endpoint extends Sender, Receiver {
	public abstract void stop();

	public abstract void start();

	public void setCtx(ChannelHandlerContext ctx);

	public abstract IpPortPair getRemoteAddress();

	public abstract void setAddr(InetSocketAddress paramInetSocketAddress);
}
