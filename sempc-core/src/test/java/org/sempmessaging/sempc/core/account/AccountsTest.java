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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
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

		verify(account).connect(eq(accountConfig));
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
	public void accountsStatusChangedEventContainsConsolidatedStatusForAllAccounts() {
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
			assertEquals(new NumConversations(12), allAccountsStatus.numConversations());
			assertEquals(new NumUnreadConversations(3), allAccountsStatus.numUnreadConversations());
		});
		subscriberCaptor.getValue().accountStatusChanged(accountStatus);
	}


	@Test
	public void accountsStatusChangedEventDoesNotContainConsolidatedStatusIfThereIsOnlyOneAccount() {
		AccountConfiguration accountConfig = mock(AccountConfiguration.class);
		when(configRepo.listAll()).thenReturn(Arrays.asList(accountConfig));
		Account account = mock(Account.class);
		when(accountProvider.get()).thenReturn(account);

		accounts.connect();

		ArgumentCaptor<AccountStatusChangedEvent> subscriberCaptor = ArgumentCaptor.forClass(AccountStatusChangedEvent.class);
		verify(account).subscribe(any(AccountStatusChangedEvent.class), subscriberCaptor.capture());

		AccountStatus accountStatus = new AccountStatus(ConnectionStatus.CONNECTED, new AccountName("test"), new NumConversations(5), new NumUnreadConversations(2));
		eventTestRule.subscribeMandatory(accounts, accounts.accountsStatusChangedEvent(), (statusList) -> {
			assertEquals(1, statusList.size());
			AccountStatus allAccountsStatus = statusList.get(0);
			assertNotNull(allAccountsStatus);
			assertNotEquals(new AccountName("All"), allAccountsStatus.accountName());
		});
		subscriberCaptor.getValue().accountStatusChanged(accountStatus);
	}

	@Test
	public void overallConnectionStatusIsErrorWhenOneConnectionIsInError() {
		AccountConfiguration accountConfig = mock(AccountConfiguration.class);
		when(configRepo.listAll()).thenReturn(Arrays.asList(accountConfig));
		Account account = mock(Account.class);
		when(accountProvider.get()).thenReturn(account);

		accounts.connect();

		ArgumentCaptor<AccountStatusChangedEvent> subscriberCaptor = ArgumentCaptor.forClass(AccountStatusChangedEvent.class);
		verify(account).subscribe(any(AccountStatusChangedEvent.class), subscriberCaptor.capture());

		subscriberCaptor.getValue().accountStatusChanged(new AccountStatus(ConnectionStatus.UNKNOWN, new AccountName("test_1"), new NumConversations(7), new NumUnreadConversations(1)));
		subscriberCaptor.getValue().accountStatusChanged(new AccountStatus(ConnectionStatus.CONNECTING, new AccountName("test_2"), new NumConversations(7), new NumUnreadConversations(1)));
		subscriberCaptor.getValue().accountStatusChanged(new AccountStatus(ConnectionStatus.ERROR, new AccountName("test_3"), new NumConversations(7), new NumUnreadConversations(1)));

		AccountStatus accountStatus = new AccountStatus(ConnectionStatus.CONNECTED, new AccountName("test_4"), new NumConversations(5), new NumUnreadConversations(2));
		eventTestRule.subscribeMandatory(accounts, accounts.accountsStatusChangedEvent(), (statusList) -> {
			AccountStatus allAccountsStatus = statusList.get(0);
			assumeNotNull(allAccountsStatus);
			assumeThat(allAccountsStatus.accountName(), is(new AccountName("All")));

			assertEquals(ConnectionStatus.ERROR, allAccountsStatus.connectionStatus());
		});
		subscriberCaptor.getValue().accountStatusChanged(accountStatus);
	}

	@Test
	public void overallConnectionStatusIsUnknownWhenOneConnectionIsInUnknownAndThereIsNoError() {
		AccountConfiguration accountConfig = mock(AccountConfiguration.class);
		when(configRepo.listAll()).thenReturn(Arrays.asList(accountConfig));
		Account account = mock(Account.class);
		when(accountProvider.get()).thenReturn(account);

		accounts.connect();

		ArgumentCaptor<AccountStatusChangedEvent> subscriberCaptor = ArgumentCaptor.forClass(AccountStatusChangedEvent.class);
		verify(account).subscribe(any(AccountStatusChangedEvent.class), subscriberCaptor.capture());

		subscriberCaptor.getValue().accountStatusChanged(new AccountStatus(ConnectionStatus.UNKNOWN, new AccountName("test_1"), new NumConversations(7), new NumUnreadConversations(1)));
		subscriberCaptor.getValue().accountStatusChanged(new AccountStatus(ConnectionStatus.CONNECTING, new AccountName("test_2"), new NumConversations(7), new NumUnreadConversations(1)));
		subscriberCaptor.getValue().accountStatusChanged(new AccountStatus(ConnectionStatus.CONNECTING, new AccountName("test_3"), new NumConversations(7), new NumUnreadConversations(1)));

		AccountStatus accountStatus = new AccountStatus(ConnectionStatus.CONNECTED, new AccountName("test_4"), new NumConversations(5), new NumUnreadConversations(2));
		eventTestRule.subscribeMandatory(accounts, accounts.accountsStatusChangedEvent(), (statusList) -> {
			AccountStatus allAccountsStatus = statusList.get(0);
			assumeNotNull(allAccountsStatus);
			assumeThat(allAccountsStatus.accountName(), is(new AccountName("All")));

			assertEquals(ConnectionStatus.UNKNOWN, allAccountsStatus.connectionStatus());
		});
		subscriberCaptor.getValue().accountStatusChanged(accountStatus);
	}

	@Test
	public void overallConnectionStatusIsConnectingWhenOneConnectionIsInConnectingAndThereIsNoErrorOrUnknownStatus() {
		AccountConfiguration accountConfig = mock(AccountConfiguration.class);
		when(configRepo.listAll()).thenReturn(Arrays.asList(accountConfig));
		Account account = mock(Account.class);
		when(accountProvider.get()).thenReturn(account);

		accounts.connect();

		ArgumentCaptor<AccountStatusChangedEvent> subscriberCaptor = ArgumentCaptor.forClass(AccountStatusChangedEvent.class);
		verify(account).subscribe(any(AccountStatusChangedEvent.class), subscriberCaptor.capture());

		subscriberCaptor.getValue().accountStatusChanged(new AccountStatus(ConnectionStatus.CONNECTED, new AccountName("test_1"), new NumConversations(7), new NumUnreadConversations(1)));
		subscriberCaptor.getValue().accountStatusChanged(new AccountStatus(ConnectionStatus.CONNECTED, new AccountName("test_2"), new NumConversations(7), new NumUnreadConversations(1)));
		subscriberCaptor.getValue().accountStatusChanged(new AccountStatus(ConnectionStatus.CONNECTING, new AccountName("test_3"), new NumConversations(7), new NumUnreadConversations(1)));

		AccountStatus accountStatus = new AccountStatus(ConnectionStatus.CONNECTED, new AccountName("test_4"), new NumConversations(5), new NumUnreadConversations(2));
		eventTestRule.subscribeMandatory(accounts, accounts.accountsStatusChangedEvent(), (statusList) -> {
			AccountStatus allAccountsStatus = statusList.get(0);
			assumeNotNull(allAccountsStatus);
			assumeThat(allAccountsStatus.accountName(), is(new AccountName("All")));

			assertEquals(ConnectionStatus.CONNECTING, allAccountsStatus.connectionStatus());
		});
		subscriberCaptor.getValue().accountStatusChanged(accountStatus);
	}

	@Test
	public void overallConnectionStatusIsConnectedWhenAllConnectionsAreConnected() {
		AccountConfiguration accountConfig = mock(AccountConfiguration.class);
		when(configRepo.listAll()).thenReturn(Arrays.asList(accountConfig));
		Account account = mock(Account.class);
		when(accountProvider.get()).thenReturn(account);

		accounts.connect();

		ArgumentCaptor<AccountStatusChangedEvent> subscriberCaptor = ArgumentCaptor.forClass(AccountStatusChangedEvent.class);
		verify(account).subscribe(any(AccountStatusChangedEvent.class), subscriberCaptor.capture());

		subscriberCaptor.getValue().accountStatusChanged(new AccountStatus(ConnectionStatus.CONNECTED, new AccountName("test_1"), new NumConversations(7), new NumUnreadConversations(1)));
		subscriberCaptor.getValue().accountStatusChanged(new AccountStatus(ConnectionStatus.CONNECTED, new AccountName("test_2"), new NumConversations(7), new NumUnreadConversations(1)));
		subscriberCaptor.getValue().accountStatusChanged(new AccountStatus(ConnectionStatus.CONNECTED, new AccountName("test_3"), new NumConversations(7), new NumUnreadConversations(1)));

		AccountStatus accountStatus = new AccountStatus(ConnectionStatus.CONNECTED, new AccountName("test_4"), new NumConversations(5), new NumUnreadConversations(2));
		eventTestRule.subscribeMandatory(accounts, accounts.accountsStatusChangedEvent(), (statusList) -> {
			AccountStatus allAccountsStatus = statusList.get(0);
			assumeNotNull(allAccountsStatus);
			assumeThat(allAccountsStatus.accountName(), is(new AccountName("All")));

			assertEquals(ConnectionStatus.CONNECTED, allAccountsStatus.connectionStatus());
		});
		subscriberCaptor.getValue().accountStatusChanged(accountStatus);
	}
}
