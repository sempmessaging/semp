package org.sempmessaging.libsemp.itest;

import com.google.inject.Scopes;
import net.davidtanzer.jevents.ComponentCodeGenerator;
import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.guice.EventComponentModule;
import net.davidtanzer.jevents.guice.EventComponentsProvider;
import org.sempmessaging.sempd.core.serverkeys.ServerPublicVerificationKeysService;

import static org.mockito.Mockito.mock;

public class LibSempItestModule extends EventComponentModule {
	private final ServerPublicVerificationKeysService serverPublicVerificationKeysService;

	public LibSempItestModule() {
		serverPublicVerificationKeysService = mock(ServerPublicVerificationKeysService.class);
	}

	@Override
	protected void configure() {
		EventComponentsProvider.setEventComponents(new EventComponents(new JavassistComponentCodeGenerator()));
		bind(ServerPublicVerificationKeysService.class).toInstance(serverPublicVerificationKeysService);
	}

	public ServerPublicVerificationKeysService serverPublicVerificationKeysService() {
		return serverPublicVerificationKeysService;
	}
}
