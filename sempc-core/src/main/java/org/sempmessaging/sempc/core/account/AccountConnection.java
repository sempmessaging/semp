package org.sempmessaging.sempc.core.account;

import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventProvider;
import org.sempmessaging.sempc.core.account.config.AccountConnectionConfiguration;

public interface AccountConnection extends EventProvider {
	@Event
	public ConnectionStatusChangedEvent connectionStatusChanged();

	void connect(Account account, AccountConnectionConfiguration accountConnectionConfiguration);
}
