package org.sempmessaging.libsemp.itest;

import net.davidtanzer.jevents.guice.EventComponentModule;
import org.sempmessaging.sempd.core.serverkeys.ServerPublicVerificationKeysService;

import static org.mockito.Mockito.mock;

public class LibSempItestModule extends EventComponentModule {
	private final ServerPublicVerificationKeysService serverPublicVerificationKeysService;

	public LibSempItestModule() {
		serverPublicVerificationKeysService = mock(ServerPublicVerificationKeysService.class);
	}

	@Override
	protected void configure() {
		bind(ServerPublicVerificationKeysService.class).toInstance(serverPublicVerificationKeysService);
	}

	public ServerPublicVerificationKeysService serverPublicVerificationKeysService() {
		return serverPublicVerificationKeysService;
	}
}
