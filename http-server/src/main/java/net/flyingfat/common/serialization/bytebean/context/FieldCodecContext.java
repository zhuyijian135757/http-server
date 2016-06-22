package net.flyingfat.common.serialization.bytebean.context;

import java.lang.reflect.Field;

import net.flyingfat.common.serialization.bytebean.codec.FieldCodecProvider;
import net.flyingfat.common.serialization.bytebean.codec.NumberCodec;
import net.flyingfat.common.serialization.bytebean.field.ByteFieldDesc;

public interface FieldCodecContext extends FieldCodecProvider {
	ByteFieldDesc getFieldDesc();

	Field getField();

	NumberCodec getNumberCodec();

	int getByteSize();
}
