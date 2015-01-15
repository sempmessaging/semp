package org.sempmessaging.sempc.core.account;

import net.davidtanzer.value.StringValue;
import org.sempmessaging.libsemp.arguments.Args;

public class AccountStatus {
	private final ConnectionStatus connectionStatus;

	public AccountStatus(final ConnectionStatus connectionStatus) {
		Args.notNull(connectionStatus, "connectionStatus");

		this.connectionStatus = connectionStatus;
	}

	public ConnectionStatus connectionStatus() {
		return connectionStatus;
	}

	public String accountName() {
		return "Private Account";
	}
}
