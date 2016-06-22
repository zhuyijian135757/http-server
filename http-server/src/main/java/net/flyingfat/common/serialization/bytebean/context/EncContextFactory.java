package net.flyingfat.common.serialization.bytebean.context;

import net.flyingfat.common.serialization.bytebean.field.ByteFieldDesc;

public interface EncContextFactory {
	EncContext createEncContext(Object paramObject, Class<?> paramClass,
			ByteFieldDesc paramByteFieldDesc);
}
