package org.sempmessaging.libsemp.io;

public interface NewDataEvent {
	void newDataAvailable(byte[] array, int numBytesRead);
}
