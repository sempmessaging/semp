package org.sempmessaging.sempd.protocol.io;

import org.sempmessaging.libsemp.connection.ReadableConnection;

import java.nio.channels.WritableByteChannel;

public interface ClientConnection extends ReadableConnection {
	WritableByteChannel writableChannel();
}
