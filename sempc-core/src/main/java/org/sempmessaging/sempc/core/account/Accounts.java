package org.sempmessaging.sempc.core.account;

import com.google.inject.Inject;
import com.google.inject.Provider;
import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.core.account.config.AccountConfiguration;
import org.sempmessaging.sempc.core.account.config.AccountConfigurationRepository;
import org.sempmessaging.sempc.core.account.value.AccountName;
import org.sempmessaging.sempc.core.account.value.ConnectionStatus;
import org.sempmessaging.sempc.core.account.value.NumConversations;
import org.sempmessaging.sempc.core.account.value.NumUnreadConversations;

import java.util.*;

public abstract class Accounts extends EventComponent {
	private AccountConfigurationRepository accountConfigurationRepository;
	private Provider<Account> accountProvider;
	private Map<AccountName, AccountStatus> accountStatuses = new HashMap<>();

	@Event
	public abstract AccountsStatusChangedEvent accountsStatusChangedEvent();

	public void connect() {
		assert accountConfigurationRepository != null : "Configuration repository must be set by dependency injection or test setup.";
		List<AccountConfiguration> accountConfigurations = accountConfigurationRepository.listAll();
		assert accountConfigurations != null : "The configuration repository never returns a null list";

		accountConfigurations.forEach((config) -> {
			assert accountProvider != null : "Account provider must be set by dependency injection or test setup.";
			Account account = accountProvider.get();
			assert account != null : "Account provider always returns a valid account.";

			account.subscribe(account.accountStatusChanged(), this::someAccountStatusChanged);
			account.connect();
		});
	}

	private void someAccountStatusChanged(AccountStatus accountStatus) {
		accountStatuses.put(accountStatus.accountName(), accountStatus);

		List<AccountStatus> statuses = new ArrayList<>();
		statuses.addAll(accountStatuses.values());
		Collections.sort(statuses, (a, b) -> a.accountName().value().compareTo(b.accountName().value()));

		gatherConsolidatedStatusIfNecessary(statuses);

		send(accountsStatusChangedEvent()).accountStatusChanged(statuses);
	}

	private void gatherConsolidatedStatusIfNecessary(List<AccountStatus> statuses) {
		if(statuses.size() > 1) {
			AccountStatus allAccountsStatus = gatherConsolidatedStatusOfAllAccounts(statuses);
			statuses.add(0, allAccountsStatus);
		}
	}

	private AccountStatus gatherConsolidatedStatusOfAllAccounts(List<AccountStatus> statuses) {
		int numConversations = 0;
		int numUnreadConversations = 0;
		ConnectionStatus connectionStatus = ConnectionStatus.CONNECTED;

		for (AccountStatus status : statuses) {
			numConversations += status.numConversations().value();
			numUnreadConversations += status.numUnreadConversations().value();
			connectionStatus = computeNewConnectionStatus(connectionStatus, status);
		}

		return new AccountStatus(connectionStatus, new AccountName("All"), new NumConversations(numConversations), new NumUnreadConversations(numUnreadConversations));
	}

	private ConnectionStatus computeNewConnectionStatus(ConnectionStatus connectionStatus, AccountStatus status) {
		if(status.connectionStatus() == ConnectionStatus.ERROR) {
			connectionStatus = ConnectionStatus.ERROR;
		}
		if(connectionStatus != connectionStatus.ERROR && status.connectionStatus()==ConnectionStatus.UNKNOWN) {
			connectionStatus = ConnectionStatus.UNKNOWN;
		}
		if(connectionStatus != ConnectionStatus.ERROR && connectionStatus != ConnectionStatus.UNKNOWN && status.connectionStatus()==ConnectionStatus.CONNECTING) {
			connectionStatus = ConnectionStatus.CONNECTING;
		}
		return connectionStatus;
	}

	@Inject
	public void setAccountConfigurationRepository(final AccountConfigurationRepository accountConfigurationRepository) {
		Args.notNull(accountConfigurationRepository, "accountConfigurationRepository");
		Args.setOnce(this.accountConfigurationRepository, "accountConfigurationRepository");

		this.accountConfigurationRepository = accountConfigurationRepository;
	}

	@Inject
	public void setAccountProvider(final Provider<Account> accountProvider) {
		Args.notNull(accountProvider, "accountProvider");
		Args.setOnce(this.accountProvider, "accountProvider");

		this.accountProvider = accountProvider;
	}
}
