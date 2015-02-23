package org.sempmessaging.sempc.core.account;

import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.sempc.core.account.config.AccountConfiguration;

public abstract class Account extends EventComponent {
	@Event
	public abstract AccountStatusChangedEvent accountStatusChanged();

	public void connect(AccountConfiguration eq) {

	}


}
