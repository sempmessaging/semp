package org.sempmessaging.libsemp.connection;

import org.sempmessaging.libsemp.connection.ReadableConnection;

public interface ConnectionClosedEvent {
	void connectionClosed(ReadableConnection clientConnection);
}
