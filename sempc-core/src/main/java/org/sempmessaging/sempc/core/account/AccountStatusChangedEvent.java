package org.sempmessaging.sempc.core.account;

import java.util.List;

public interface AccountStatusChangedEvent {
	void accountStatusChanged(List<AccountStatus> connectionStatuses);
}
