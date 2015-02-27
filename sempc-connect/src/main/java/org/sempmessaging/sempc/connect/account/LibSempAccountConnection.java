package org.sempmessaging.sempc.connect.account;

import com.google.inject.Inject;
import com.google.inject.Provider;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.connection.ServerConnection;
import org.sempmessaging.libsemp.request.serverinfo.GetServerInfoRequest;
import org.sempmessaging.libsemp.request.serverinfo.ServerName;
import org.sempmessaging.libsemp.server.ServerInformation;
import org.sempmessaging.sempc.connect.server.ServerInformationCache;
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
	private ServerInformationCache serverInformationCache;

	@Override
	public void connect(Account account, AccountConnectionConfiguration accountConnectionConfiguration) {
		Args.notNull(account, "account");
		Args.setOnce(this.account, "account");
		Args.notNull(accountConnectionConfiguration, "accountConnectionConfiguration");
		Args.setOnce(this.accountConnectionConfiguration, "accountConnectionConfiguration");

		this.account = account;
		this.accountConnectionConfiguration = accountConnectionConfiguration;

		reConnectIfNecessary();
	}

	private void reConnectIfNecessary() {
		if(serverConnection == null || !serverConnection.isConnected()) {
			establishServerConnection();
		}
	}

	private void establishServerConnection() {
		serverConnection = serverConnectionProvider.get();

		try {
			connectToServer();
			logConnectionIn();
		} catch (IllegalStateException e) {
			String errorMessage = "Could not connect to server \"" + accountConnectionConfiguration.host() + ":" + accountConnectionConfiguration.port() + "\"";
			send(connectionStatusChanged()).connectionStatusChanged(ConnectionStatus.ERROR, new ConnectionStatusMessage(errorMessage));
		}
	}

	private void connectToServer() {
		serverConnection.connect(accountConnectionConfiguration.host().value(), accountConnectionConfiguration.port().value());
		send(connectionStatusChanged()).connectionStatusChanged(ConnectionStatus.CONNECTING, new ConnectionStatusMessage("Initial connection established."));
	}

	private void logConnectionIn() {
		ServerInformation cachedServerInformation = serverInformationCache.getServerInformation();
		if(cachedServerInformation == null) {
			GetServerInfoRequest getServerInfoRequest = serverConnection.newRequest(GetServerInfoRequest.class);
			getServerInfoRequest.subscribe(getServerInfoRequest.serverInformationReceivedEvent(), this::serverInformationForLoginReceived);
			getServerInfoRequest.requestKeysForServer(new ServerName(accountConnectionConfiguration.host().value()));
		} else {
			logInWithServerInformation(cachedServerInformation);
		}
	}

	private void serverInformationForLoginReceived(final ServerInformation serverInformation) {
		serverInformationCache.putServerInformation(serverInformation);
		logInWithServerInformation(serverInformation);
	}

	private void logInWithServerInformation(final ServerInformation serverInformation) {
		send(connectionStatusChanged()).connectionStatusChanged(ConnectionStatus.CONNECTING, new ConnectionStatusMessage("Logging in..."));
		//TODO implement me
	}

	@Inject
	public void setServerConnectionProvider(final Provider<ServerConnection> serverConnectionProvider) {
		Args.notNull(serverConnectionProvider, "serverConnectionProvider");
		Args.setOnce(this.serverConnectionProvider, "serverConnectionProvider");

		this.serverConnectionProvider = serverConnectionProvider;
	}

	@Inject
	public void setServerInformationCache(ServerInformationCache serverInformationCache) {
		Args.notNull(serverInformationCache, "serverInformationCache");
		Args.setOnce(this.serverInformationCache, "serverInformationCache");

		this.serverInformationCache = serverInformationCache;
	}
}
