package net.flyingfat.common.lang.transport;

public interface Sender {
	void send(Object paramObject);

	void send(Object paramObject, Receiver paramReceiver);
}
