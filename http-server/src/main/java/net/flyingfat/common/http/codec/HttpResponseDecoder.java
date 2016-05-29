package net.flyingfat.common.http.codec;

import java.util.List;

import net.flyingfat.common.lang.ByteUtil;
import net.flyingfat.common.lang.DESUtil;
import net.flyingfat.common.serialization.bytebean.codec.AnyCodec;
import net.flyingfat.common.serialization.bytebean.codec.DefaultCodecProvider;
import net.flyingfat.common.serialization.bytebean.codec.DefaultNumberCodecs;
import net.flyingfat.common.serialization.bytebean.codec.array.LenArrayCodec;
import net.flyingfat.common.serialization.bytebean.codec.array.LenListCodec;
import net.flyingfat.common.serialization.bytebean.codec.bean.BeanFieldCodec;
import net.flyingfat.common.serialization.bytebean.codec.bean.EarlyStopBeanCodec;
import net.flyingfat.common.serialization.bytebean.codec.primitive.ByteCodec;
import net.flyingfat.common.serialization.bytebean.codec.primitive.CStyleStringCodec;
import net.flyingfat.common.serialization.bytebean.codec.primitive.FloatCodec;
import net.flyingfat.common.serialization.bytebean.codec.primitive.IntCodec;
import net.flyingfat.common.serialization.bytebean.codec.primitive.LenByteArrayCodec;
import net.flyingfat.common.serialization.bytebean.codec.primitive.LongCodec;
import net.flyingfat.common.serialization.bytebean.codec.primitive.ShortCodec;
import net.flyingfat.common.serialization.bytebean.context.DecContextFactory;
import net.flyingfat.common.serialization.bytebean.context.DecResult;
import net.flyingfat.common.serialization.bytebean.context.DefaultDecContextFactory;
import net.flyingfat.common.serialization.bytebean.context.DefaultEncContextFactory;
import net.flyingfat.common.serialization.bytebean.field.DefaultField2Desc;
import net.flyingfat.common.serialization.protocol.meta.MsgCode2Type;
import net.flyingfat.common.serialization.protocol.xip.XipHeader;
import net.flyingfat.common.serialization.protocol.xip.XipSignal;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponseDecoder
  extends MessageToMessageDecoder<Object>
{
  private static final Logger logger = LoggerFactory.getLogger(HttpResponseDecoder.class);
  private BeanFieldCodec byteBeanCodec;
  private MsgCode2Type typeMetaInfo;
  private int dumpBytes = 256;
  private boolean isDebugEnabled;
  private byte[] encryptKey;
  
  
  protected void decode(ChannelHandlerContext ctx, Object msg, List<Object> out)
    throws Exception
  {
    if (logger.isDebugEnabled()) {
      logger.debug("decode {}", msg);
    }
    if ((msg instanceof HttpResponse))
    {
      FullHttpResponse response = (FullHttpResponse)msg;
      if (response.getStatus().code() != 200) {
    	  out.add(msg);
      }
      ByteBuf content = response.content();
      if (!content.isReadable()) {
    	  out.add(msg);
      }
      byte[] bytes = new byte[content.readableBytes()];
      content.readBytes(bytes);
      if ((logger.isDebugEnabled()) && (this.isDebugEnabled)) {
        logger.debug(ByteUtil.bytesAsHexString(bytes, this.dumpBytes));
      }
      XipSignal signal = decodeXipSignal(bytes);
      if ((logger.isDebugEnabled()) && (this.isDebugEnabled)) {
        logger.debug("decoded signal:{}", ToStringBuilder.reflectionToString(signal));
      }
      out.add(signal);
    }
    out.add(msg);
  }
  
  public XipSignal decodeXipSignal(byte[] bytes)
    throws Exception
  {
    XipHeader header = (XipHeader)getByteBeanCodec().decode(getByteBeanCodec().getDecContextFactory().createDecContext(bytes, XipHeader.class, null, null)).getValue();

    Class<?> type = this.typeMetaInfo.find(header.getMessageCode());
    if (null == type) {
      throw new RuntimeException("unknow message code:" + header.getMessageCode());
    }
    byte[] bodyBytes = ArrayUtils.subarray(bytes, XipHeader.HEADER_LENGTH, bytes.length);
    if (header!=null && header.getReserved()!=XipHeader.CONTENT_DES) {
      try
      {
        bodyBytes = DESUtil.decrypt(bodyBytes, getEncryptKey());
      }
      catch (Exception e)
      {
        throw new RuntimeException("decode decryption failed." + e.getMessage());
      }
    }
    XipSignal signal = (XipSignal)getByteBeanCodec().decode(getByteBeanCodec().getDecContextFactory().createDecContext(bodyBytes, type, null, null)).getValue();
    if (null != signal) {
      signal.setIdentification(header.getTransactionAsUUID());
    }
    return signal;
  }
  
  public void setByteBeanCodec(BeanFieldCodec byteBeanCodec)
  {
    this.byteBeanCodec = byteBeanCodec;
  }
  
  public BeanFieldCodec getByteBeanCodec()
  {
    if (this.byteBeanCodec == null)
    {
      DefaultCodecProvider codecProvider = new DefaultCodecProvider();
      

      codecProvider.addCodec(new AnyCodec()).addCodec(new ByteCodec()).addCodec(new ShortCodec()).addCodec(new IntCodec()).addCodec(new LongCodec()).addCodec(new CStyleStringCodec()).addCodec(new LenByteArrayCodec()).addCodec(new LenListCodec()).addCodec(new LenArrayCodec()).addCodec(new FloatCodec());
      


      EarlyStopBeanCodec byteBeanCodec = new EarlyStopBeanCodec(new DefaultField2Desc());
      codecProvider.addCodec(byteBeanCodec);
      
      DefaultEncContextFactory encContextFactory = new DefaultEncContextFactory();
      DefaultDecContextFactory decContextFactory = new DefaultDecContextFactory();
      
      encContextFactory.setCodecProvider(codecProvider);
      encContextFactory.setNumberCodec(DefaultNumberCodecs.getLittleEndianNumberCodec());
      
      decContextFactory.setCodecProvider(codecProvider);
      decContextFactory.setNumberCodec(DefaultNumberCodecs.getLittleEndianNumberCodec());
      
      byteBeanCodec.setDecContextFactory(decContextFactory);
      byteBeanCodec.setEncContextFactory(encContextFactory);
      
      this.byteBeanCodec = byteBeanCodec;
    }
    return this.byteBeanCodec;
  }
  
  public void setTypeMetaInfo(MsgCode2Type typeMetaInfo)
  {
    this.typeMetaInfo = typeMetaInfo;
  }
  
  public void setDumpBytes(int dumpBytes)
  {
    this.dumpBytes = dumpBytes;
  }
  
  public MsgCode2Type getTypeMetaInfo()
  {
    return this.typeMetaInfo;
  }
  
  public int getDumpBytes()
  {
    return this.dumpBytes;
  }
  
  public boolean isDebugEnabled()
  {
    return this.isDebugEnabled;
  }
  
  public void setDebugEnabled(boolean isDebugEnabled)
  {
    this.isDebugEnabled = isDebugEnabled;
  }
  
  public byte[] getEncryptKey()
  {
    return this.encryptKey;
  }
  
  public void setEncryptKey(byte[] encryptKey)
  {
    this.encryptKey = encryptKey;
  }

}
