package org.sempmessaging.libsemp.connection;

import java.nio.channels.ReadableByteChannel;

public interface ReadableConnection {
	ReadableByteChannel readableChannel();
}
