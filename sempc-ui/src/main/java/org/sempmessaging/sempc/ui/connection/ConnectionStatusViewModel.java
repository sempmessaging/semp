package org.sempmessaging.sempc.ui.connection;

import com.google.inject.Inject;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.core.account.AccountStatus;
import org.sempmessaging.sempc.core.account.Accounts;
import org.sempmessaging.sempc.core.account.ConnectionStatus;
import org.sempmessaging.sempc.ui.viewmodel.AbstractViewModel;
import org.sempmessaging.sempc.ui.viewmodel.Property;

import java.util.List;

public abstract class ConnectionStatusViewModel extends AbstractViewModel {
	public Property<ConnectionStatus> overallConnectionStatus = newProperty(ConnectionStatus.class);

	@Inject
	public void setAccounts(final Accounts accounts) {
		Args.notNull(accounts, "accounts");
		accounts.subscribe(accounts.accountStatusChangedEvent(), this::updateConnectionStatuses);

		overallConnectionStatus.set(ConnectionStatus.UNKNOWN);
	}

	private void updateConnectionStatuses(final List<AccountStatus> accountStatuses) {
		ConnectionStatus overallStatus = ConnectionStatus.CONNECTED;

		for(AccountStatus accountStatus : accountStatuses) {
			if(accountStatus.connectionStatus() == ConnectionStatus.ERROR) {
				overallStatus = ConnectionStatus.ERROR;
				break;
			}
			if(accountStatus.connectionStatus() == ConnectionStatus.CONNECTING) {
				overallStatus = ConnectionStatus.CONNECTING;
				break;
			}
		}

		overallConnectionStatus.set(overallStatus);
	}


}
