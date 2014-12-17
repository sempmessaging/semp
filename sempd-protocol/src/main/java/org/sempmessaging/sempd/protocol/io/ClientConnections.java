package org.sempmessaging.sempd.protocol.io;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.connection.ConnectionListener;
import org.sempmessaging.libsemp.connection.ReadableConnection;
import org.sempmessaging.libsemp.io.MessageParser;
import org.sempmessaging.libsemp.io.NioJSONSender;
import org.sempmessaging.libsemp.roundrobin.RoundRobin;
import org.sempmessaging.sempd.protocol.requests.RequestHandlers;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ClientConnections implements StartClientConnections {
	private final RoundRobin roundRobin;

	private List<StorableClientConnection> connections = new ArrayList<>();
	private Provider<ConnectionListener> connectionListenerProvider;
	private Provider<ClientConnectionResponseWriter> responseWriterProvider;
	private Provider<MessageParser> messageParserProvider;
	private Provider<RequestHandlers> requestHandlersProvider;

	@Inject
	public ClientConnections(final RoundRobin roundRobin, final Provider<ConnectionListener> connectionListenerProvider,
							 final Provider<ClientConnectionResponseWriter> responseWriterProvider,
							 final Provider<MessageParser> messageParserProvider, final Provider<RequestHandlers> requestHandlersProvider) {
		Args.notNull(roundRobin, "roundRobin");
		Args.notNull(connectionListenerProvider, "connectionListenerProvider");
		Args.notNull(responseWriterProvider, "responseWriterProvider");
		Args.notNull(messageParserProvider, "messageParserProvider");
		Args.notNull(requestHandlersProvider, "requestHandlersProvider");

		this.roundRobin = roundRobin;
		this.connectionListenerProvider = connectionListenerProvider;
		this.responseWriterProvider = responseWriterProvider;
		this.messageParserProvider = messageParserProvider;
		this.requestHandlersProvider = requestHandlersProvider;
	}

	@Override
	public void startClientConnection(final SocketChannel socketChannel) {
		Args.notNull(socketChannel, "socketChannel");

		StorableClientConnection clientConnection = setupClientConnection(socketChannel);
		registerClientConnection(clientConnection);
	}

	private StorableClientConnection setupClientConnection(final SocketChannel socketChannel) {
		ConnectionListener connectionListener = connectionListenerProvider.get();
		ClientConnectionResponseWriter responseWriter = responseWriterProvider.get();
		NioJSONSender jsonSender = new NioJSONSender(socketChannel);
		StorableClientConnection clientConnection = new StorableClientConnection(socketChannel, connectionListener, responseWriter, jsonSender);

		connectionListener.listenTo(clientConnection);
		connectionListener.subscribe(connectionListener.connectionClosedEvent(), this::shutdownClientConnection);
		connectionListener.subscribe(connectionListener.ioExceptionEvent(), (e) -> {
			throw new UnsupportedOperationException("Not yet implemented");
		});

		responseWriter.setJsonSender(jsonSender);

		MessageParser messageParser = messageParserProvider.get();
		RequestHandlers requestHandlers = requestHandlersProvider.get();

		assert messageParser != null : "Message parser cannot be null because it came from dependency injection.";
		assert requestHandlers != null : "Request handler cannot be null because it came from dependency injection.";

		connectionListener.subscribe(connectionListener.newDataEvent(), messageParser::addMessageData);
		messageParser.subscribe(messageParser.newMessageDataEvent(), requestHandlers::processJsonData);
		requestHandlers.subscribe(requestHandlers.responseDataAvailableEvent(), responseWriter::writeResponse);

		return clientConnection;
	}

	private void shutdownClientConnection(final ReadableConnection connection) {
		StorableClientConnection storableClientConnection = (StorableClientConnection) connection;
		roundRobin.unregister(storableClientConnection.connectionListener());
		roundRobin.unregister(storableClientConnection.responseWriter());
		storableClientConnection.jsonSender().disconnect();

		connections.remove(connection);
	}

	private void registerClientConnection(final StorableClientConnection clientConnection) {
		roundRobin.register(clientConnection.connectionListener());
		roundRobin.register(clientConnection.responseWriter());
		connections.add(clientConnection);
	}

}
