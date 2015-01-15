package org.sempmessaging.sempc.ui;

import net.davidtanzer.jevents.guice.EventComponentModule;
import org.sempmessaging.sempc.ui.connection.ConnectionStatusViewModel;

public class SempcUiModule extends EventComponentModule {
	@Override
	protected void configure() {
		bindEventComponent(ConnectionStatusViewModel.class);
	}
}
