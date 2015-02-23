package org.sempmessaging.sempc.core.account;

import org.sempmessaging.sempc.core.account.config.AccountConnectionConfiguration;

public interface AccountConnection {
	void connect(Account account, AccountConnectionConfiguration accountConnectionConfiguration);
}
