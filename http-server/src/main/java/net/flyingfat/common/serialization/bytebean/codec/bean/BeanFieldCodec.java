package net.flyingfat.common.serialization.bytebean.codec.bean;

import net.flyingfat.common.serialization.bytebean.codec.ByteFieldCodec;
import net.flyingfat.common.serialization.bytebean.context.DecContextFactory;
import net.flyingfat.common.serialization.bytebean.context.EncContextFactory;

public interface BeanFieldCodec extends ByteFieldCodec {
	int getStaticByteSize(Class<?> paramClass);

	DecContextFactory getDecContextFactory();

	EncContextFactory getEncContextFactory();
}
