package net.flyingfat.common.serialization.bytebean.codec;

public interface NumberCodec {
	String convertCharset(String paramString);

	byte[] short2Bytes(short paramShort, int paramInt);

	byte[] int2Bytes(int paramInt1, int paramInt2);

	byte[] long2Bytes(long paramLong, int paramInt);

	byte[] float2Bytes(float paramFloat, int paramInt);

	byte[] double2Bytes(double paramDouble, int paramInt);

	short bytes2Short(byte[] paramArrayOfByte, int paramInt);

	int bytes2Int(byte[] paramArrayOfByte, int paramInt);

	long bytes2Long(byte[] paramArrayOfByte, int paramInt);

	float bytes2Float(byte[] paramArrayOfByte, int paramInt);

	double bytes2Double(byte[] paramArrayOfByte, int paramInt);
}
