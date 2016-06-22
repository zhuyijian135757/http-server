package net.flyingfat.common.serialization.bytebean.field;

import java.lang.reflect.Field;

public interface Field2Desc {
	ByteFieldDesc genDesc(Field paramField);
}
