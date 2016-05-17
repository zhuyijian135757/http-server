package net.flyingfat.common.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.AttributeKey;
import net.flyingfat.common.http.endpoint.Endpoint;
import net.flyingfat.common.lang.Propertyable;
import net.flyingfat.common.lang.transport.Sender;

public class TransportUtil
{
  private static final AttributeKey<Endpoint> endpointKey =AttributeKey.newInstance("Endpoint");
  
  public static void attachEndpointToCtx(ChannelHandlerContext ctx, Endpoint endpoint)
  {
	  ctx.attr(endpointKey).set(endpoint);;
  }
  
  public static void detachEndpointToCtx(ChannelHandlerContext ctx)
  {
	  ctx.attr(endpointKey).remove();
  }
  
  public static Endpoint getEndpointOfCtx(ChannelHandlerContext ctx)
  {
    return ctx.attr(endpointKey).get();
  }
  
  public static Object attachSender(Object propertyable, Sender sender)
  {
    if ((propertyable instanceof Propertyable)) {
      ((Propertyable)propertyable).setProperty("TRANSPORT_SENDER", sender);
    }
    return propertyable;
  }
  
  public static Sender getSenderOf(Object propertyable)
  {
    if ((propertyable instanceof Propertyable)) {
      return (Sender)((Propertyable)propertyable).getProperty("TRANSPORT_SENDER");
    }
    return null;
  }
  
  public static Object attachRequest(Object propertyable, HttpRequest request)
  {
    if ((propertyable instanceof Propertyable)) {
      ((Propertyable)propertyable).setProperty("httpRequest", request);
    }
    return propertyable;
  }
  
  public static HttpRequest getRequestOf(Object propertyable)
  {
    if ((propertyable instanceof Propertyable)) {
      return (HttpRequest)((Propertyable)propertyable).getProperty("httpRequest");
    }
    return null;
  }

}
