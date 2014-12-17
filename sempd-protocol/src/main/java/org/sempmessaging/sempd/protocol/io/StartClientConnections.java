package org.sempmessaging.sempd.protocol.io;

import java.nio.channels.SocketChannel;

public interface StartClientConnections {
	void startClientConnection(SocketChannel socketChannel);
}
