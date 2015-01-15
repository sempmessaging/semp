package org.sempmessaging.sempc.core;

import com.google.inject.Scopes;
import net.davidtanzer.jevents.guice.EventComponentModule;
import org.sempmessaging.sempc.core.account.Accounts;

public class SempcCoreModule extends EventComponentModule {
	@Override
	protected void configure() {
		bindEventComponent(Accounts.class).in(Scopes.SINGLETON);
	}
}
