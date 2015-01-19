package org.sempmessaging.sempd.protocol.io;

import com.google.inject.Provider;
import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import org.junit.Before;
import org.junit.Test;
import org.sempmessaging.libsemp.connection.ConnectionListener;
import org.sempmessaging.libsemp.connection.ReadableConnection;
import org.sempmessaging.libsemp.io.MessageParser;
import org.sempmessaging.libsemp.request.JSONSender;
import org.sempmessaging.libsemp.roundrobin.RoundRobin;
import org.sempmessaging.sempd.protocol.requests.RequestHandlers;

import java.nio.channels.SocketChannel;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ClientConnectionsTest {
	private RoundRobin roundRobin;
	private ClientConnections clientConnections;
	private TestClientConnectionListener clientConnectionListener;
	private ClientConnectionResponseWriter responseWriter;
	private Provider<RequestHandlers> requestHandlersProvider;
	private Provider<ConnectionListener> connectionListenerProvider;
	private Provider<ClientConnectionResponseWriter> responseWriterProvider;
	private Provider<MessageParser> messageParserProvider;

	@Before
	public void setup() {
		roundRobin = mock(RoundRobin.class);
		requestHandlersProvider = mock(Provider.class);
		connectionListenerProvider = mock(Provider.class);
		responseWriterProvider = mock(Provider.class);
		messageParserProvider = mock(Provider.class);
		clientConnections = new ClientConnections(roundRobin, connectionListenerProvider, responseWriterProvider, messageParserProvider, requestHandlersProvider);

		clientConnectionListener = spy(new EventComponents(new JavassistComponentCodeGenerator()).createComponent(TestClientConnectionListener.class));
		when(connectionListenerProvider.get()).thenReturn(clientConnectionListener);

		MessageParser messageParser = mock(MessageParser.class);
		when(messageParserProvider.get()).thenReturn(messageParser);

		RequestHandlers requestHandlers = mock(RequestHandlers.class);
		when(requestHandlersProvider.get()).thenReturn(requestHandlers);

		responseWriter = mock(ClientConnectionResponseWriter.class);
		when(responseWriterProvider.get()).thenReturn(responseWriter);
	}

	@Test
	public void startsClientConnectionThatListensToGivenChannel() {
		SocketChannel channel = mock(SocketChannel.class);
		clientConnections.startClientConnection(channel);

		verify(clientConnectionListener).listenTo(any(ClientConnection.class));
	}

	@Test
	public void startsClientConnectionThatWritesToGivenJsonSender() {
		SocketChannel channel = mock(SocketChannel.class);
		clientConnections.startClientConnection(channel);

		verify(responseWriter).setJsonSender(any(JSONSender.class));
	}

	@Test
	public void registersClientConnectionListenerWithRoundRobin() {
		SocketChannel channel = mock(SocketChannel.class);
		clientConnections.startClientConnection(channel);

		verify(roundRobin).register(clientConnectionListener);
	}

	@Test
	public void unregistersClientConnectionListenerFromRoundRobinAtShutdown() {
		SocketChannel channel = mock(SocketChannel.class);
		clientConnections.startClientConnection(channel);

		clientConnectionListener.sendConnectionClosedEvent();

		verify(roundRobin).unregister(clientConnectionListener);
	}

	@Test
	public void registersResponseWriterWithRoundRobin() {
		SocketChannel channel = mock(SocketChannel.class);
		clientConnections.startClientConnection(channel);

		verify(roundRobin).register(responseWriter);
	}

	@Test
	public void unregistersResponseWriterFromRoundRobinAtShutdown() {
		SocketChannel channel = mock(SocketChannel.class);
		clientConnections.startClientConnection(channel);

		clientConnectionListener.sendConnectionClosedEvent();

		verify(roundRobin).unregister(responseWriter);
	}

	@Test
	public void thereIsExactlyOneRequestHandlersPerClientConnection() throws Throwable {
		SocketChannel channel = mock(SocketChannel.class);

		clientConnections.startClientConnection(channel);

		verify(requestHandlersProvider, times(1)).get();
	}

	public static abstract class TestClientConnectionListener extends ConnectionListener {
		private ClientConnection connection;

		@Override
		public void listenTo(final ReadableConnection clientConnection) {
			super.listenTo(clientConnection);
			this.connection = (ClientConnection) clientConnection;
		}

		public void sendConnectionClosedEvent() {
			send(connectionClosedEvent()).connectionClosed(connection);
		}
	}
}
