package org.sempmessaging.libsemp.connection;

import com.google.inject.Inject;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.io.MessageParser;
import org.sempmessaging.libsemp.io.NioJSONSender;
import org.sempmessaging.libsemp.request.Request;
import org.sempmessaging.libsemp.roundrobin.RoundRobin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;

public class ServerConnection implements ReadableConnection {
	private SocketChannel socketChannel;
	private String host;
	private int port;
	private NioJSONSender jsonSender;
	private RoundRobin roundRobin;
	private ConnectionListener connectionListener;
	private ConnectionRequests connectionRequests;
	private MessageParser messageParser;

	public ServerConnection() {
	}

	public void connect(final String host, final int port) {
		Args.notEmpty(host, "host");

		this.host = host;
		this.port = port;

		connectToServerOfThisConnection();
		setupConnectionHandlers();
	}

	private void connectToServerOfThisConnection() {
		try {
			socketChannel = SocketChannel.open();
			socketChannel.connect(new InetSocketAddress(host, port));
			socketChannel.configureBlocking(false);
		} catch (IOException e) {
			throw new IllegalStateException("Could not connect to server \""+host+":"+port+"\"", e);
		}
	}

	private void setupConnectionHandlers() {
		jsonSender = new NioJSONSender(socketChannel);

		assert connectionListener != null : "connectionListener must be set by dependency injection or test setup";
		connectionListener.listenTo(this);
		assert messageParser != null : "messageParser must be set by dependency injection or test setup";
		connectionListener.subscribe(connectionListener.newDataEvent(), messageParser::addMessageData);
		assert connectionRequests != null : "connectionRequests must be set by dependency injection or test setup";
		messageParser.subscribe(messageParser.newMessageDataEvent(), connectionRequests::processJsonData);

		assert roundRobin != null : "roundRobin must be set by dependency injection or test setup";
		roundRobin.register(connectionListener);
	}

	public void disconnect() {
		disconnectHandlers();
		checkSocketChannelCanBeDisconnected();
		disconnectSocketChannel();
	}

	private void disconnectHandlers() {
		assert jsonSender != null : "jsonSender is usually created in connect() - Did you call this method before?";
		jsonSender.disconnect();
		assert roundRobin != null : "roundRobin must be set by dependency injection or test setup";
		roundRobin.unregister(connectionListener);
	}

	private void disconnectSocketChannel() {
		try {
			socketChannel.close();
		} catch (IOException e) {
			throw new IllegalStateException("Could not close connection to server \""+host+":"+port+"\"", e);
		}
	}

	private void checkSocketChannelCanBeDisconnected() {
		if(!socketChannel.isOpen()) {
			throw new IllegalStateException("Tried to close socket channel that is not open. Did you call disconnect twice or before connect?");
		}
	}

	public boolean isConnected() {
		return socketChannel.isOpen();
	}

	public <T extends Request> T newRequest(final Class<T> requestClass) {
		T request = connectionRequests.newRequest(requestClass);

		assert jsonSender != null : "jsonSender must be set by dependency injection or test setup.";
		request.sendJsonTo(jsonSender);
		return request;
	}

	@Inject
	void setConnectionListener(final ConnectionListener connectionListener) {
		Args.notNull(connectionListener, "connectionListener");
		this.connectionListener = connectionListener;
	}

	@Inject
	void setMessageParser(final MessageParser messageParser) {
		Args.notNull(messageParser, "messageParser");
		this.messageParser = messageParser;
	}

	@Inject
	void setRoundRobin(final RoundRobin roundRobin) {
		Args.notNull(roundRobin, "roundRobin");
		this.roundRobin = roundRobin;
	}

	@Inject
	void setConnectionRequests(final ConnectionRequests connectionRequests) {
		Args.notNull(connectionRequests, "connectionRequests");
		this.connectionRequests = connectionRequests;
	}

	@Override
	public ReadableByteChannel readableChannel() {
		return socketChannel;
	}
}
