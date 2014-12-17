package org.sempmessaging.libsemp.connection;

import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.io.IOExceptionEvent;
import org.sempmessaging.libsemp.io.NewDataEvent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public abstract class ConnectionListener extends EventComponent implements Runnable {
	private static final int BUFFER_CAPACITY = 4096;

	private ReadableByteChannel channel;
	private ByteBuffer buffer;
	private final byte[] readBytes;
	private ReadableConnection connection;

	@Event
	public abstract NewDataEvent newDataEvent();
	@Event
	public abstract ConnectionClosedEvent connectionClosedEvent();
	@Event
	public abstract IOExceptionEvent ioExceptionEvent();

	public ConnectionListener() {
		buffer = ByteBuffer.allocateDirect(BUFFER_CAPACITY);
		readBytes = new byte[BUFFER_CAPACITY];
	}

	public void listenTo(final ReadableConnection connection) {
		Args.notNull(connection, "connection");
		this.connection = connection;
		this.channel = connection.readableChannel();
	}

	@Override
	public void run() {
		try {
			int numBytesRead = channel.read(buffer);
			if(numBytesRead > 0) {
				buffer.flip();

				buffer.get(readBytes, 0, numBytesRead);
				send(newDataEvent()).newDataAvailable(readBytes, numBytesRead);

				buffer.clear();
			} else if(numBytesRead < 0) {
				send(connectionClosedEvent()).connectionClosed(connection);
			}
		} catch (IOException e) {
			send(ioExceptionEvent()).ioException(e);
		}
	}
}
