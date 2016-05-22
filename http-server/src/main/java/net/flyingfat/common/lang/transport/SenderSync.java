package net.flyingfat.common.lang.transport;

import java.util.concurrent.TimeUnit;

public interface SenderSync
{
  public abstract Object sendAndWait(Object paramObject);
  
  public abstract Object sendAndWait(Object paramObject, long paramLong, TimeUnit paramTimeUnit);
}
