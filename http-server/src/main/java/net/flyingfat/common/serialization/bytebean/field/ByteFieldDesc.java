package net.flyingfat.common.serialization.bytebean.field;

import java.lang.reflect.Field;
import java.util.Comparator;

public interface ByteFieldDesc {
	public static final Comparator<ByteFieldDesc> comparator = new Comparator<ByteFieldDesc>() {
		public int compare(ByteFieldDesc desc1, ByteFieldDesc desc2) {
			int ret = desc1.getIndex() - desc2.getIndex();
			if (0 == ret) {
				throw new RuntimeException("field1:" + desc1.getField()
						+ "/field2:" + desc2.getField()
						+ " has the same index value, internal error.");
			}
			return ret;
		}

	};

	int getIndex();

	int getByteSize();

	Field getField();

	Class<?> getFieldType();

	boolean hasLength();

	int getLength(Object paramObject);

	int getStringLengthInBytes(Object paramObject);

	String getCharset();

	String getDescription();

	int getFixedLength();

}
