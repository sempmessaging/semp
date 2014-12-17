package org.sempmessaging.sempd.protocol;

import com.google.inject.Scopes;
import net.davidtanzer.jevents.guice.EventComponentModule;
import org.sempmessaging.sempd.protocol.async.AsyncExecutor;
import org.sempmessaging.sempd.protocol.async.ThreadPoolAsyncExecutor;
import org.sempmessaging.libsemp.connection.ConnectionListener;
import org.sempmessaging.sempd.protocol.io.ClientConnections;
import org.sempmessaging.sempd.protocol.io.StartClientConnections;
import org.sempmessaging.sempd.protocol.requests.handler.RequestHandlerRunner;
import org.sempmessaging.sempd.protocol.requests.RequestHandlers;
import org.sempmessaging.sempd.protocol.requests.RequestRouter;

public class SempdProtocolModule extends EventComponentModule {
	@Override
	protected void configure() {
		bind(StartClientConnections.class).to(ClientConnections.class).in(Scopes.SINGLETON);
		bind(AsyncExecutor.class).to(ThreadPoolAsyncExecutor.class).in(Scopes.SINGLETON);

		bindEventComponent(RequestHandlers.class);
		bindEventComponent(RequestRouter.class);
		bindEventComponent(RequestHandlerRunner.class);

	}
}
