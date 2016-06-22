package net.flyingfat.common.serialization.bytebean.codec;

import net.flyingfat.common.serialization.bytebean.context.DecContext;
import net.flyingfat.common.serialization.bytebean.context.DecResult;
import net.flyingfat.common.serialization.bytebean.context.EncContext;

public interface ByteFieldCodec {
	FieldCodecCategory getCategory();

	Class<?>[] getFieldType();

	DecResult decode(DecContext paramDecContext);

	byte[] encode(EncContext paramEncContext);
}
