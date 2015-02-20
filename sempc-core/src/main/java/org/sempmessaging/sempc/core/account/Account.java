package org.sempmessaging.sempc.core.account;

import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventComponent;

public abstract class Account extends EventComponent {
	@Event
	public abstract AccountStatusChangedEvent accountStatusChanged();

	public void connect() {

	}


}
