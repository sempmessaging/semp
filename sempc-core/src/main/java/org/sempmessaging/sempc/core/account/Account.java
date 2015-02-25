package org.sempmessaging.sempc.core.account;

import com.google.inject.Inject;
import com.google.inject.Provider;
import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventComponent;
import net.davidtanzer.value.SingleValue;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.core.account.config.AccountConfiguration;
import org.sempmessaging.sempc.core.account.value.ConnectionStatus;
import org.sempmessaging.sempc.core.account.value.ConnectionStatusMessage;
import org.sempmessaging.sempc.core.account.value.NumConversations;
import org.sempmessaging.sempc.core.account.value.NumUnreadConversations;

public abstract class Account extends EventComponent {
	private AccountConfiguration configuration;
	private AccountConnection accountConnection;

	@Event
	public abstract AccountStatusChangedEvent accountStatusChanged();

	public void connect(AccountConfiguration configuration) {
		configure(configuration);

		assert accountConnection != null : "Account connection must be set by dependency injection or test setup.";
		accountConnection.subscribe(accountConnection.connectionStatusChanged(), this::connectionStatusChanged);
		accountConnection.connect(this, configuration.connectionConfiguration());
	}

	private void connectionStatusChanged(ConnectionStatus connectionStatus, ConnectionStatusMessage statusMessage) {
		send(accountStatusChanged()).accountStatusChanged(new AccountStatus(connectionStatus, configuration.accountName(), SingleValue.empty(NumConversations.class), SingleValue.empty(NumUnreadConversations.class)));
	}

	private void configure(AccountConfiguration configuration) {
		Args.notNull(configuration, "configuration");
		Args.setOnce(this.configuration, "configuration");

		this.configuration = configuration;
	}

	@Inject
	public void setAccountConnection(final AccountConnection accountConnection) {
		Args.notNull(accountConnection, "accountConnection");
		Args.setOnce(this.accountConnection, "accountConnection");

		this.accountConnection = accountConnection;
	}
}
