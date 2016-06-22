package net.flyingfat.common.serialization.bytebean.context;

public interface DecContext extends FieldCodecContext {
	Object getDecOwner();

	byte[] getDecBytes();

	Class<?> getDecClass();

	DecContextFactory getDecContextFactory();
}
