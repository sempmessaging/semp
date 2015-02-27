package org.sempmessaging.sempc.core.account;

import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.core.account.value.*;

public class AccountStatus {
	private final ConnectionStatus connectionStatus;
	private final AccountName accountName;
	private final NumConversations numConversations;
	private final NumUnreadConversations numUnreadConversations;
	private final ConnectionStatusMessage connectionStatusMessage;

	public AccountStatus(final ConnectionStatus connectionStatus, final AccountName accountName, final NumConversations numConversations, final NumUnreadConversations numUnreadConversations, final ConnectionStatusMessage connectionStatusMessage) {
		Args.notNull(connectionStatus, "connectionStatus");
		Args.notNull(accountName, "accountName");
		Args.notNull(numConversations, "numConversations");
		Args.notNull(numUnreadConversations, "numUnreadConversations");
		Args.notNull(connectionStatusMessage, "connectionStatusMessage");

		this.connectionStatus = connectionStatus;
		this.accountName = accountName;
		this.numConversations = numConversations;
		this.numUnreadConversations = numUnreadConversations;
		this.connectionStatusMessage = connectionStatusMessage;
	}

	public ConnectionStatus connectionStatus() {
		return connectionStatus;
	}

	public AccountName accountName() {
		return accountName;
	}

	public NumConversations numConversations() {
		return numConversations;
	}

	public NumUnreadConversations numUnreadConversations() {
		return numUnreadConversations;
	}

	public ConnectionStatusMessage connectionStatusMessage() {
		return connectionStatusMessage;
	}
}
