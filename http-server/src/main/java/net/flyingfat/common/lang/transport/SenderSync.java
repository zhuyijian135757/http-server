package net.flyingfat.common.lang.transport;

import java.util.concurrent.TimeUnit;

public interface SenderSync {
	Object sendAndWait(Object paramObject);

	Object sendAndWait(Object paramObject, long paramLong,
			TimeUnit paramTimeUnit);
}
