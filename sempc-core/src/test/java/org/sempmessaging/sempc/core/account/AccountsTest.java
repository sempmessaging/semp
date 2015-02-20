package org.sempmessaging.sempc.core.account;

import com.google.inject.Provider;
import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.sempmessaging.sempc.core.account.config.AccountConfiguration;
import org.sempmessaging.sempc.core.account.config.AccountConfigurationRepository;
import org.sempmessaging.sempc.core.account.value.AccountName;
import org.sempmessaging.sempc.core.account.value.ConnectionStatus;
import org.sempmessaging.sempc.core.account.value.NumConversations;
import org.sempmessaging.sempc.core.account.value.NumUnreadConversations;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountsTest {
	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	private Accounts accounts;
	private AccountConfigurationRepository configRepo;
	private Provider<Account> accountProvider;

	@Before
	public void setup() {
		accounts = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(Accounts.class);

		configRepo = mock(AccountConfigurationRepository.class);
		accounts.setAccountConfigurationRepository(configRepo);

		accountProvider = mock(Provider.class);
		accounts.setAccountProvider(accountProvider);
	}

	@Test
	public void connectGetsListOfAccountsFromConfigurationRepository() {
		accounts.connect();

		verify(configRepo).listAll();
	}

	@Test
	public void createsNewAccountForEachAccountConfiguration() {
		AccountConfiguration accountConfig = mock(AccountConfiguration.class);
		when(configRepo.listAll()).thenReturn(Arrays.asList(accountConfig));
		Account account = mock(Account.class);
		when(accountProvider.get()).thenReturn(account);

		accounts.connect();

		verify(accountProvider).get();
	}

	@Test
	public void callsConnectOnEachAccount() {
		AccountConfiguration accountConfig = mock(AccountConfiguration.class);
		when(configRepo.listAll()).thenReturn(Arrays.asList(accountConfig));
		Account account = mock(Account.class);
		when(accountProvider.get()).thenReturn(account);

		accounts.connect();

		verify(account).connect();
	}

	@Test
	public void sendsEventWhenAccountStatusChanges() {
		AccountConfiguration accountConfig = mock(AccountConfiguration.class);
		when(configRepo.listAll()).thenReturn(Arrays.asList(accountConfig));
		Account account = mock(Account.class);
		when(accountProvider.get()).thenReturn(account);

		accounts.connect();

		ArgumentCaptor<AccountStatusChangedEvent> subscriberCaptor = ArgumentCaptor.forClass(AccountStatusChangedEvent.class);
		verify(account).subscribe(any(AccountStatusChangedEvent.class), subscriberCaptor.capture());

		eventTestRule.subscribeMandatory(accounts, accounts.accountsStatusChangedEvent(), (statusList) -> {
			assertNotNull(statusList);
		});
		subscriberCaptor.getValue().accountStatusChanged(new AccountStatus(ConnectionStatus.CONNECTED, new AccountName("test"), new NumConversations(5), new NumUnreadConversations(2)));
	}

	@Test
	public void accountsStatusChangedEventContainsTheStatusOfTheChangedAccount() {
		AccountConfiguration accountConfig = mock(AccountConfiguration.class);
		when(configRepo.listAll()).thenReturn(Arrays.asList(accountConfig));
		Account account = mock(Account.class);
		when(accountProvider.get()).thenReturn(account);

		accounts.connect();

		ArgumentCaptor<AccountStatusChangedEvent> subscriberCaptor = ArgumentCaptor.forClass(AccountStatusChangedEvent.class);
		verify(account).subscribe(any(AccountStatusChangedEvent.class), subscriberCaptor.capture());

		AccountStatus accountStatus = new AccountStatus(ConnectionStatus.CONNECTED, new AccountName("test"), new NumConversations(5), new NumUnreadConversations(2));
		eventTestRule.subscribeMandatory(accounts, accounts.accountsStatusChangedEvent(), (statusList) -> {
			AccountStatus changedAccountStatus = null;
			for(AccountStatus status : statusList) {
				if(status.accountName().equals(accountStatus.accountName())) {
					changedAccountStatus = status;
				}
			}
			assertNotNull(changedAccountStatus);
			assertEquals(accountStatus.connectionStatus(), changedAccountStatus.connectionStatus());
			assertEquals(accountStatus.numConversations(), changedAccountStatus.numConversations());
			assertEquals(accountStatus.numUnreadConversations(), changedAccountStatus.numUnreadConversations());
		});
		subscriberCaptor.getValue().accountStatusChanged(accountStatus);
	}

	@Test
	public void accountsStatusChangedEventContainsCummulatedStatusForAllAccounts() {
		AccountConfiguration accountConfig = mock(AccountConfiguration.class);
		when(configRepo.listAll()).thenReturn(Arrays.asList(accountConfig));
		Account account = mock(Account.class);
		when(accountProvider.get()).thenReturn(account);

		accounts.connect();

		ArgumentCaptor<AccountStatusChangedEvent> subscriberCaptor = ArgumentCaptor.forClass(AccountStatusChangedEvent.class);
		verify(account).subscribe(any(AccountStatusChangedEvent.class), subscriberCaptor.capture());

		subscriberCaptor.getValue().accountStatusChanged(new AccountStatus(ConnectionStatus.CONNECTING, new AccountName("test_1"), new NumConversations(7), new NumUnreadConversations(1)));

		AccountStatus accountStatus = new AccountStatus(ConnectionStatus.CONNECTED, new AccountName("test_2"), new NumConversations(5), new NumUnreadConversations(2));
		eventTestRule.subscribeMandatory(accounts, accounts.accountsStatusChangedEvent(), (statusList) -> {
			AccountStatus allAccountsStatus = statusList.get(0);
			assertNotNull(allAccountsStatus);
			assertEquals(new AccountName("All"), allAccountsStatus.accountName());
			assertEquals(ConnectionStatus.CONNECTING, allAccountsStatus.connectionStatus());
			assertEquals(new NumConversations(12), allAccountsStatus.numConversations());
			assertEquals(new NumUnreadConversations(3), allAccountsStatus.numUnreadConversations());
		});
		subscriberCaptor.getValue().accountStatusChanged(accountStatus);
	}
}