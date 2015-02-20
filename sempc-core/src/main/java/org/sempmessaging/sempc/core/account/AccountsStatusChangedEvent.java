package org.sempmessaging.sempc.core.account;

import java.util.List;

public interface AccountsStatusChangedEvent {
	void accountStatusChanged(List<AccountStatus> connectionStatuses);
}
