package org.sempmessaging.sempd.protocol.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class TcpSocketServerTest {
	private TcpSocketServer server;
	private StartClientConnections clientConnections;

	@Before
	public void setup() {
		clientConnections = mock(StartClientConnections.class);
		server = new TcpSocketServer(clientConnections);
		server.start();
	}

	@After
	public void teardown() {
		server.shutdown();
	}

	@Test
	public void doesNotCreateClientConnectionWhenNoConnectionIsAvailable() {
		server.run();
		verify(clientConnections, never()).startClientConnection(any(SocketChannel.class));
	}

	@Test
	public void runDoesNothingAfterACleanShutdown() {
		server.shutdown();
		server.run();
	}

	@Test
	public void serverListensOnPort9930() throws IOException {
		Socket socket = new Socket("localhost", 9930);
		server.run();
		socket.close();
	}

	@Test
	public void serverCreatesNewClientSessionForEveryConnection() throws IOException {
		Socket socket = new Socket("localhost", 9930);
		sleepALittle();
		server.run();
		verify(clientConnections).startClientConnection(any(SocketChannel.class));
		socket.close();
	}

	private void sleepALittle() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			//Ignored on purpose.
		}
	}
}
