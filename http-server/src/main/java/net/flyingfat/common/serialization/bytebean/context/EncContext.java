package net.flyingfat.common.serialization.bytebean.context;

public interface EncContext extends FieldCodecContext {
	Object getEncObject();

	Class<?> getEncClass();

	EncContextFactory getEncContextFactory();
}
