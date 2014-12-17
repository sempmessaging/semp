package org.sempmessaging.libsemp;

import net.davidtanzer.jevents.guice.EventComponentModule;
import org.sempmessaging.libsemp.connection.ConnectionListener;
import org.sempmessaging.libsemp.connection.ConnectionRequests;
import org.sempmessaging.libsemp.connection.ServerConnectionRequests;
import org.sempmessaging.libsemp.io.MessageParser;
import org.sempmessaging.libsemp.request.serverpublickeys.GetServerPublicVerificationKeysRequest;

public class LibSempModule extends EventComponentModule {
	@Override
	protected void configure() {
		bind(ConnectionRequests.class).to(ServerConnectionRequests.class);

		bindEventComponent(ConnectionListener.class);
		bindEventComponent(MessageParser.class);
		bindEventComponent(GetServerPublicVerificationKeysRequest.class);
	}
}
