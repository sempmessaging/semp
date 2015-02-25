package org.sempmessaging.sempc.connect.account;

import com.google.inject.Provider;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.connection.ServerConnection;
import org.sempmessaging.sempc.core.account.Account;
import org.sempmessaging.sempc.core.account.AccountConnection;
import org.sempmessaging.sempc.core.account.config.AccountConnectionConfiguration;
import org.sempmessaging.sempc.core.account.value.ConnectionStatus;
import org.sempmessaging.sempc.core.account.value.ConnectionStatusMessage;

public abstract class LibSempAccountConnection extends EventComponent implements AccountConnection {
	private Provider<ServerConnection> serverConnectionProvider;
	private Account account;
	private AccountConnectionConfiguration accountConnectionConfiguration;
	private ServerConnection serverConnection;

	@Override
	public void connect(Account account, AccountConnectionConfiguration accountConnectionConfiguration) {
		Args.notNull(account, "account");
		Args.setOnce(this.account, "account");
		Args.notNull(accountConnectionConfiguration, "accountConnectionConfiguration");
		Args.setOnce(this.accountConnectionConfiguration, "accountConnectionConfiguration");

		this.account = account;
		this.accountConnectionConfiguration = accountConnectionConfiguration;

		reConnect();
	}

	private void reConnect() {
		if(serverConnection == null || !serverConnection.isConnected()) {
			serverConnection = serverConnectionProvider.get();

			try {
				serverConnection.connect(accountConnectionConfiguration.host(), accountConnectionConfiguration.port());
			} catch (IllegalStateException e) {
				String errorMessage = "Could not connect to server \"" + accountConnectionConfiguration.host() + ":" + accountConnectionConfiguration.port() + "\"";
				send(connectionStatusChanged()).connectionStatusChanged(ConnectionStatus.ERROR, new ConnectionStatusMessage(errorMessage));
			}
		}
	}

	public void setServerConnectionProvider(final Provider<ServerConnection> serverConnectionProvider) {
		Args.notNull(serverConnectionProvider, "serverConnectionProvider");
		Args.setOnce(this.serverConnectionProvider, "serverConnectionProvider");

		this.serverConnectionProvider = serverConnectionProvider;
	}
}
