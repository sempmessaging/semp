package org.sempmessaging.sempc.ui.menu.accounts;

import com.google.inject.Inject;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.core.account.AccountStatus;
import org.sempmessaging.sempc.core.account.Accounts;
import org.sempmessaging.sempc.ui.viewmodel.AbstractViewModel;
import org.sempmessaging.sempc.ui.viewmodel.Property;

import java.util.List;

public abstract class AccountsMenuComponentViewModel extends AbstractViewModel {
	private Property<List<AccountStatus>> accountStatuses = newListProperty(AccountStatus.class);

	@Inject
	public void setAccounts(final Accounts accounts) {
		Args.notNull(accounts, "accounts");
		accounts.subscribe(accounts.accountsStatusChangedEvent(), this::updateAccountStatuses);

		//overallConnectionStatus.set(ConnectionStatus.UNKNOWN);
	}

	private void updateAccountStatuses(final List<AccountStatus> accountStatuses) {
		this.accountStatuses.set(accountStatuses);

		//computeOverallConnectionStatus(accountStatuses);
	}

	public Property<List<AccountStatus>> accountStatuses() {
		return accountStatuses;
	}
}
