package org.sempmessaging.sempc.ui.menu.accounts;

import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.sempmessaging.sempc.core.account.AccountStatus;
import org.sempmessaging.sempc.core.account.AccountStatusChangedEvent;
import org.sempmessaging.sempc.core.account.Accounts;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountsMenuComponentViewModelTest {
	private AccountsMenuComponentViewModel viewModel;

	@Before
	public void setup() {
		viewModel = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(AccountsMenuComponentViewModel.class);

	}

	@Test
	public void updatesAccountStatusesPropertyWhenAccountsChange() {
		Accounts accounts = mock(Accounts.class);
		viewModel.setAccounts(accounts);
		ArgumentCaptor<AccountStatusChangedEvent> eventHandlerCaptor = ArgumentCaptor.forClass(AccountStatusChangedEvent.class);
		verify(accounts).subscribe(any(AccountStatusChangedEvent.class), eventHandlerCaptor.capture());

		AccountStatus accountStatus = mock(AccountStatus.class);
		eventHandlerCaptor.getValue().accountStatusChanged(Arrays.asList(accountStatus));

		List<AccountStatus> accountStatuses = viewModel.accountStatuses().get();
		assertNotNull(accountStatuses);
		assertEquals(1, accountStatuses.size());
		assertEquals(accountStatus, accountStatuses.get(0));
	}
}