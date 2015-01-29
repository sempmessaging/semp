package org.sempmessaging.sempc.core.account;

import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventComponent;

import java.util.Arrays;

public abstract class Accounts extends EventComponent {
	@Event
	public abstract AccountStatusChangedEvent accountStatusChangedEvent();

	public void connect() {
		new Thread() {
			@Override
			public void run() {
				try {
					sleep(1000L);
				} catch (InterruptedException e) {
					//e.printStackTrace();
				}

				//send(accountStatusChangedEvent()).accountStatusChanged(Arrays.asList(new AccountStatus(ConnectionStatus.CONNECTING)));
				try {
					sleep(1000L);
				} catch (InterruptedException e) {
					//e.printStackTrace();
				}

				//send(accountStatusChangedEvent()).accountStatusChanged(Arrays.asList(new AccountStatus(ConnectionStatus.CONNECTED)));
			}
		}.start();
	}
}
