package org.sempmessaging.sempc.ui;

import com.google.inject.Scopes;
import net.davidtanzer.jevents.guice.EventComponentModule;
import org.sempmessaging.sempc.ui.connection.ConnectionStatusViewModel;
import org.sempmessaging.sempc.ui.menu.MainMenu;
import org.sempmessaging.sempc.ui.menu.MainMenuButton;
import org.sempmessaging.sempc.ui.menu.MainMenuCloseButton;

public class SempcUiModule extends EventComponentModule {
	@Override
	protected void configure() {
		bindEventComponent(MainMenuButton.class).in(Scopes.SINGLETON);
		bindEventComponent(MainMenuCloseButton.class).in(Scopes.SINGLETON);
		bindEventComponent(MainMenu.class).in(Scopes.SINGLETON);

		bindEventComponent(ConnectionStatusViewModel.class).in(Scopes.SINGLETON);
	}
}
