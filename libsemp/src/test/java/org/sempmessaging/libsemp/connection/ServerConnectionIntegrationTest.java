package org.sempmessaging.libsemp.connection;

import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.sempmessaging.libsemp.io.MessageParser;
import org.sempmessaging.libsemp.io.NewDataEvent;
import org.sempmessaging.libsemp.io.NewMessageDataEvent;
import org.sempmessaging.libsemp.io.NioJSONSender;
import org.sempmessaging.libsemp.request.JSONSender;
import org.sempmessaging.libsemp.request.Request;
import org.sempmessaging.libsemp.roundrobin.RoundRobin;

import java.net.InetSocketAddress;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ServerConnectionIntegrationTest {
	private static final int TEST_PORT = 19931;

	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	private ServerConnection serverConnection;
	private ServerSocketChannel serverSocketChannel;
	private ConnectionListener connectionListener;
	private RoundRobin roundRobin;
	private ConnectionRequests connectionRequests;
	private MessageParser messageParser;

	@Before
	public void setup() throws Exception {
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress(TEST_PORT));
		serverSocketChannel.configureBlocking(false);

		connectionListener = mock(ConnectionListener.class);
		roundRobin = mock(RoundRobin.class);
		connectionRequests = mock(ConnectionRequests.class);
		messageParser = mock(MessageParser.class);

		serverConnection = new ServerConnection();
		serverConnection.setConnectionListener(connectionListener);
		serverConnection.setRoundRobin(roundRobin);
		serverConnection.setConnectionRequests(connectionRequests);
		serverConnection.setMessageParser(messageParser);

		serverConnection.connect("localhost", TEST_PORT);
	}

	@After
	public void teardown() throws Exception {
		try {
			if(serverConnection.isConnected()) {
				serverConnection.disconnect();
			}
		} finally {
			serverSocketChannel.close();
		}
	}

	@Test
	public void connectCreatesANewConnectionToTheServer() throws Exception {
		Thread.sleep(10);
		SocketChannel socketChannel = serverSocketChannel.accept();
		assertNotNull(socketChannel);
	}

	@Test
	public void connectTellsConnectionListenerTheCurrentConnection() {
		verify(connectionListener).listenTo(any(ReadableConnection.class));
	}

	@Test
	public void connectWiresNewDataEventOfConnectionListener() {
		verify(connectionListener).subscribe(any(NewDataEvent.class), any(NewDataEvent.class));
	}

	@Test
	public void connectWiresNewDataEventOfMessageParser() {
		verify(messageParser).subscribe(any(NewMessageDataEvent.class), any(NewMessageDataEvent.class));
	}

	@Test
	public void connectRegistersConnectionListenerWithRoundRobin() {
		verify(roundRobin).register(connectionListener);
	}

	@Test
	public void disconnectUnregistersConnectionListenerFromRoundRobin() {
		serverConnection.disconnect();
		verify(roundRobin).unregister(connectionListener);
	}

	@Test
	public void newRequestCreatesANewRequestUsingRequestsManager() {
		Request expectedRequest = mock(Request.class);
		when(connectionRequests.newRequest(any())).thenReturn(expectedRequest);

		Request request = serverConnection.newRequest(Request.class);

		assertEquals(expectedRequest, request);
	}

	@Test
	public void newRequestConfiguresTheNewRequestToSendToTheJsonSender() {
		Request expectedRequest = mock(Request.class);
		when(connectionRequests.newRequest(any())).thenReturn(expectedRequest);

		Request request = serverConnection.newRequest(Request.class);

		verify(request).sendJsonTo(any(JSONSender.class));
	}
}
