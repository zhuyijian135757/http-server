package net.flyingfat.common.serialization.protocol.meta;

import java.io.IOException;
import java.util.Collection;

import net.flyingfat.common.lang.PackageUtil;
import net.flyingfat.common.serialization.protocol.annotation.SignalCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetainfoUtils
{
  private static final Logger logger = LoggerFactory.getLogger(MetainfoUtils.class);
  private static MetainfoUtils util = new MetainfoUtils();
  private MetainfoUtils(){};
  
  public static MetainfoUtils getUtil()
  {
    return util;
  }
  
  public static DefaultMsgCode2TypeMetainfo createTypeMetainfo(Collection<String> packages)
  {
    DefaultMsgCode2TypeMetainfo typeMetainfo = new DefaultMsgCode2TypeMetainfo();
    if (null != packages) {
      for (String pkgName : packages) {
        try
        {
          String[] clsNames = PackageUtil.findClassesInPackage(pkgName, null, null);
          for (String clsName : clsNames) {
            try
            {
              ClassLoader cl = Thread.currentThread().getContextClassLoader();
              if (logger.isDebugEnabled()) {
                logger.debug("using ClassLoader {} to load Class {}",new Object[]{cl,clsName});
              }
              Class<?> cls = cl.loadClass(clsName);
              SignalCode attr = (SignalCode)cls.getAnnotation(SignalCode.class);
              if (null != attr)
              {
                int value = attr.messageCode();
                typeMetainfo.add(value, cls);
                logger.info("metainfo: add  {}  :=>  {}",value,cls);
              }
            }
            catch (ClassNotFoundException e)
            {
              logger.error("createTypeMetainfo: {}", e);
            }
          }
        }
        catch (IOException e)
        {
          logger.error("createTypeMetainfo: ", e);
        }
      }
    }
    return typeMetainfo;
  }
}
