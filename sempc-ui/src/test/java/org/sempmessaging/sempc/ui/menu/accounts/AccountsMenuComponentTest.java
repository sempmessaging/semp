package org.sempmessaging.sempc.ui.menu.accounts;

import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import org.junit.Before;
import org.junit.Test;
import org.sempmessaging.sempc.core.account.AccountStatus;
import org.sempmessaging.sempc.ui.components.List;
import org.sempmessaging.sempc.ui.viewmodel.Property;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountsMenuComponentTest {
	private AccountsMenuComponent accountsMenuComponent;
	private AccountsMenuComponentViewModel viewModel;
	private List<AccountStatus> list;

	@Before
	public void setup() {
		accountsMenuComponent = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(AccountsMenuComponent.class);

		list = mock(List.class);
		accountsMenuComponent.setAccountsMenuList(list);

		viewModel = mock(AccountsMenuComponentViewModel.class);
		accountsMenuComponent.setViewModel(viewModel);
	}

	@Test
	public void usesAccountsMenuItemRendererToRenderListItems() {
		accountsMenuComponent.initializeComponent();

		verify(list).setListItemRenderer(any(AccountsMenuItemRenderer.class));
	}

	@Test
	public void rendersListItemsFromViewModel() {
		Property<java.util.List<AccountStatus>> accountStatuses = mock(Property.class);
		when(viewModel.accountStatuses()).thenReturn(accountStatuses);

		accountsMenuComponent.initializeComponent();

		verify(list).setListContent(accountStatuses);
	}
}