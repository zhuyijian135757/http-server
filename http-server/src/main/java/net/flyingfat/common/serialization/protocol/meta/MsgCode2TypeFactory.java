package net.flyingfat.common.serialization.protocol.meta;

import java.io.IOException;
import java.util.Collection;

import net.flyingfat.common.lang.PackageUtil;
import net.flyingfat.common.serialization.protocol.annotation.SignalCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgCode2TypeFactory {
	private static final Logger logger = LoggerFactory
			.getLogger(MsgCode2TypeFactory.class);

	public DefaultMsgCode2Type createMsgCode2Type(
			Collection<String> packages) {
		DefaultMsgCode2Type typeMetainfo = new DefaultMsgCode2Type();
		if (null != packages) {
			for (String pkgName : packages) {
				try {
					String[] clsNames = PackageUtil.findClassesInPackage(
							pkgName, null, null);
					for (String clsName : clsNames) {
						try {
							ClassLoader cl = Thread.currentThread()
									.getContextClassLoader();
							if (logger.isDebugEnabled()) {
								logger.debug(
										"using ClassLoader {} to load Class {}",
										new Object[] { cl, clsName });
							}
							Class<?> cls = cl.loadClass(clsName);
							SignalCode attr = (SignalCode) cls
									.getAnnotation(SignalCode.class);
							if (null != attr) {
								int value = attr.messageCode();
								typeMetainfo.add(value, cls);
								logger.info("metainfo: add  {}  :=>  {}",
										value, cls);
							}
						} catch (ClassNotFoundException e) {
							logger.error("createTypeMetainfo: {}", e);
						}
					}
				} catch (IOException e) {
					logger.error("createTypeMetainfo: ", e);
				}
			}
		}
		return typeMetainfo;
	}
}
