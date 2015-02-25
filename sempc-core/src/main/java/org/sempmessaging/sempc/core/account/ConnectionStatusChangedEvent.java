package org.sempmessaging.sempc.core.account;

import org.sempmessaging.sempc.core.account.value.ConnectionStatus;
import org.sempmessaging.sempc.core.account.value.ConnectionStatusMessage;

public interface ConnectionStatusChangedEvent {
	void connectionStatusChanged(ConnectionStatus status, ConnectionStatusMessage statusMessage);
}
