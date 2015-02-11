package org.sempmessaging.sempc.ui.menu.accounts;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.values.CssClass;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.core.account.AccountStatus;
import org.sempmessaging.sempc.ui.HtmlComponent;
import org.sempmessaging.sempc.ui.components.List;
import org.sempmessaging.sempc.ui.components.ListItemRenderer;

public abstract class AccountsMenuComponent extends HtmlComponent {
	private AccountsMenuComponentViewModel viewModel;
	private List<AccountStatus> accountsMenuList;
	private ListItemRenderer<AccountStatus> listItemRenderer = new AccountsMenuItemRenderer();

	public AccountsMenuComponent() {
		cssClass(new CssClass("accounts-menu-list"));
	}

	@Override
	protected void initializeComponent() {
		assert accountsMenuList != null : "The list must be set by dependency injection or test setup.";
		accountsMenuList.setListContent(viewModel.accountStatuses());
		accountsMenuList.setListItemRenderer(listItemRenderer);
	}

	@Override
	protected FlowContentNode[] getInnerHtml() {
		return new FlowContentNode[] { accountsMenuList.getHtml() };
	}

	@Inject
	public void setViewModel(final AccountsMenuComponentViewModel viewModel) {
		Args.notNull(viewModel, "viewModel");
		Args.setOnce(this.viewModel, "viewModel");

		this.viewModel = viewModel;
	}

	@Inject
	public void setAccountsMenuList(@Named("accounts-menu-list") final List<AccountStatus> accountsMenuList) {
		Args.notNull(accountsMenuList, "accountsMenuList");
		Args.setOnce(this.accountsMenuList, "accountsMenuList");

		this.accountsMenuList = accountsMenuList;
	}
}
