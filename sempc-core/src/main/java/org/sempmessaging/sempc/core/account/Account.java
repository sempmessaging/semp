package org.sempmessaging.sempc.core.account;

import com.google.inject.Inject;
import com.google.inject.Provider;
import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.core.account.config.AccountConfiguration;

public abstract class Account extends EventComponent {
	private AccountConfiguration configuration;
	private AccountConnection accountConnection;

	@Event
	public abstract AccountStatusChangedEvent accountStatusChanged();

	public void connect(AccountConfiguration configuration) {
		configure(configuration);

		assert accountConnection != null : "Account connection must be set by dependency injection or test setup.";
		accountConnection.connect(this, configuration.connectionConfiguration());
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
