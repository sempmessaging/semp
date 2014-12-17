package org.sempmessaging.sempd.protocol.io;

import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.connection.ConnectionListener;
import org.sempmessaging.libsemp.io.NioJSONSender;

import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

class StorableClientConnection implements ClientConnection {
	private final NioJSONSender jsonSender;
	private final SocketChannel socketChannel;
	private final ConnectionListener connectionListener;
	private final ClientConnectionResponseWriter responseWriter;

	StorableClientConnection(final SocketChannel socketChannel, final ConnectionListener connectionListener, final ClientConnectionResponseWriter responseWriter, final NioJSONSender jsonSender) {
		Args.notNull(socketChannel, "socketChannel");
		Args.notNull(connectionListener, "connectionListener");
		Args.notNull(responseWriter, "responseWriter");
		Args.notNull(jsonSender, "jsonSender");

		this.socketChannel = socketChannel;
		this.connectionListener = connectionListener;
		this.responseWriter = responseWriter;
		this.jsonSender = jsonSender;
	}

	@Override
	public ReadableByteChannel readableChannel() {
		return socketChannel;
	}

	@Override
	public WritableByteChannel writableChannel() {
		return socketChannel;
	}

	public ConnectionListener connectionListener() {
		return connectionListener;
	}

	public ClientConnectionResponseWriter responseWriter() {
		return responseWriter;
	}

	public NioJSONSender jsonSender() {
		return jsonSender;
	}
}
