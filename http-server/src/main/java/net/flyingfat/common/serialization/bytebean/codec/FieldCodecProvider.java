package net.flyingfat.common.serialization.bytebean.codec;

public interface FieldCodecProvider {
	ByteFieldCodec getCodecOf(FieldCodecCategory paramFieldCodecCategory);

	ByteFieldCodec getCodecOf(Class<?> paramClass);
}
