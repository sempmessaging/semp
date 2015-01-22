package org.sempmessaging.sempd;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import net.davidtanzer.jevents.ComponentCodeGenerator;
import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.guice.EventComponentsProvider;

public class SempServerModule extends AbstractModule {
	@Override
	protected void configure() {
		EventComponentsProvider.setEventComponents(new EventComponents(new JavassistComponentCodeGenerator()));
	}
}
