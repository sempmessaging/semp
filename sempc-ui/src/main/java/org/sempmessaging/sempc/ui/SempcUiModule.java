package org.sempmessaging.sempc.ui;

import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import net.davidtanzer.jevents.guice.EventComponentModule;
import org.sempmessaging.sempc.core.account.AccountStatus;
import org.sempmessaging.sempc.ui.components.List;
import org.sempmessaging.sempc.ui.connection.ConnectionStatusViewModel;
import org.sempmessaging.sempc.ui.menu.MainMenu;
import org.sempmessaging.sempc.ui.menu.MainMenuButton;
import org.sempmessaging.sempc.ui.menu.MainMenuCloseButton;
import org.sempmessaging.sempc.ui.menu.accounts.AccountsMenuComponent;
import org.sempmessaging.sempc.ui.menu.accounts.AccountsMenuComponentViewModel;

public class SempcUiModule extends EventComponentModule {
	@Override
	protected void configure() {
		bindEventComponent(MainMenuButton.class).in(Scopes.SINGLETON);
		bindEventComponent(MainMenuCloseButton.class).in(Scopes.SINGLETON);
		bindEventComponent(MainMenu.class).in(Scopes.SINGLETON);
		bindEventComponent(AccountsMenuComponent.class).in(Scopes.SINGLETON);

		bindEventComponent(ConnectionStatusViewModel.class).in(Scopes.SINGLETON);
		bindEventComponent(AccountsMenuComponentViewModel.class).in(Scopes.SINGLETON);

		bindAnnotatedEventComponent(Names.named("accounts-menu-list"), new TypeLiteral<List<AccountStatus>>() {}).in(Scopes.SINGLETON);
	}
}
