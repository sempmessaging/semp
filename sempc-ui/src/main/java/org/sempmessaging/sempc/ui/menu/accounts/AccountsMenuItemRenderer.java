package org.sempmessaging.sempc.ui.menu.accounts;

import org.sempmessaging.sempc.core.account.AccountStatus;
import org.sempmessaging.sempc.ui.HtmlComponent;
import org.sempmessaging.sempc.ui.components.ListItemRenderer;

public class AccountsMenuItemRenderer implements ListItemRenderer<AccountStatus> {
	@Override
	public HtmlComponent render(final AccountStatus accountStatus) {
		return new AccountsMenuItem(accountStatus);
	}
}
