package org.sempmessaging.sempc.core.account.value;

import net.davidtanzer.value.StringValue;

public class ConnectionStatusMessage extends StringValue {
	public ConnectionStatusMessage(String value) {
		super(value);
	}

	private ConnectionStatusMessage() {
		super();
	}
}
