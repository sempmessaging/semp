package org.sempmessaging.sempc.core.account;

import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.core.account.value.AccountName;
import org.sempmessaging.sempc.core.account.value.ConnectionStatus;
import org.sempmessaging.sempc.core.account.value.NumConversations;
import org.sempmessaging.sempc.core.account.value.NumUnreadConversations;

public class AccountStatus {
	private final ConnectionStatus connectionStatus;
	private AccountName accountName;
	private NumConversations numConversations;
	private NumUnreadConversations numUnreadConversations;

	public AccountStatus(final ConnectionStatus connectionStatus, final AccountName accountName, final NumConversations numConversations, final NumUnreadConversations numUnreadConversations) {
		Args.notNull(connectionStatus, "connectionStatus");
		Args.notNull(accountName, "accountName");
		Args.notNull(numConversations, "numConversations");
		Args.notNull(numUnreadConversations, "numUnreadConversations");

		this.connectionStatus = connectionStatus;
		this.accountName = accountName;
		this.numConversations = numConversations;
		this.numUnreadConversations = numUnreadConversations;
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
}
