package net.flyingfat.common.serialization.protocol.meta;

import java.util.HashMap;
import java.util.Map;

public class DefaultMsgCode2Type
  implements MsgCode2Type
{
  private Map<Integer, Class<?>> codes = new HashMap<Integer, Class<?>>();
  
  public Class<?> find(int value)
  {
    return (Class<?>)codes.get(Integer.valueOf(value));
  }
  
  public void add(int tag, Class<?> type)
  {
    codes.put(Integer.valueOf(tag), type);
  }
  
  public Map<Integer, String> getAllMetainfo()
  {
    Map<Integer, String> ret = new HashMap<Integer, String>();
    for (Map.Entry<Integer, Class<?>> entry : codes.entrySet()) {
      ret.put(entry.getKey(), ((Class<?>)entry.getValue()).toString());
    }
    return ret;
  }
}
