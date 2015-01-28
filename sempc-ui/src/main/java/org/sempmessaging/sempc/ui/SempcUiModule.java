package org.sempmessaging.sempc.ui;

import net.davidtanzer.jevents.guice.EventComponentModule;
import org.sempmessaging.sempc.ui.connection.ConnectionStatusViewModel;
import org.sempmessaging.sempc.ui.menu.MenuOpenButton;

public class SempcUiModule extends EventComponentModule {
	@Override
	protected void configure() {
		bindEventComponent(MenuOpenButton.class);
		bindEventComponent(ConnectionStatusViewModel.class);
	}
}
