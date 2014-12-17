package org.sempmessaging.sempd.protocol.io;

import com.google.inject.Inject;
import org.sempmessaging.libsemp.arguments.Args;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TcpSocketServer implements Runnable {
	private ServerSocketChannel serverSocket;
	private StartClientConnections startClientConnections;

	private int port = 9930;

	@Inject
	public TcpSocketServer(final StartClientConnections startClientConnections) {
		Args.notNull(startClientConnections, "startClientConnections");
		this.startClientConnections = startClientConnections;
	}

	@Override
	public void run() {
		acceptSingleNewConnection();
	}

	private void acceptSingleNewConnection() {
		try {
			safelyAcceptSingleConnection();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private void safelyAcceptSingleConnection() throws IOException {
		if(serverSocket.isOpen()) {
			SocketChannel socketChannel = serverSocket.accept();
			if (socketChannel != null) {
				socketChannel.configureBlocking(false);
				startClientConnections.startClientConnection(socketChannel);
			}
		}
	}

	public void start() {
		createServerSocket();
	}

	private void createServerSocket() {
		try {
			serverSocket = ServerSocketChannel.open();
			serverSocket.bind(new InetSocketAddress(port));
			serverSocket.configureBlocking(false);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public void shutdown() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public void listenOnPort(final int port) {
		this.port = port;
	}
}
