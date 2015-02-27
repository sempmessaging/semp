package org.sempmessaging.sempc.core.account.config;

import net.davidtanzer.value.SingleValue;
import org.sempmessaging.sempc.core.account.config.value.HostName;
import org.sempmessaging.sempc.core.account.config.value.Port;

public class AccountConnectionConfiguration {
	public HostName host() {
		return SingleValue.empty(HostName.class);
	}

	public Port port() {
		return SingleValue.empty(Port.class);
	}
}
