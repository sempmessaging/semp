package org.sempmessaging.sempc.connect;

import com.google.inject.Scopes;
import net.davidtanzer.jevents.guice.EventComponentModule;
import org.sempmessaging.sempc.connect.account.LibSempAccountConnection;
import org.sempmessaging.sempc.core.account.AccountConnection;

public class SempcConnectModule extends EventComponentModule {
	@Override
	protected void configure() {
		bind(AccountConnection.class).to(LibSempAccountConnection.class).in(Scopes.SINGLETON);
	}
}
