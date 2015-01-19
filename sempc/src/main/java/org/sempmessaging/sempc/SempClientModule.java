package org.sempmessaging.sempc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import net.davidtanzer.jevents.ComponentCodeGenerator;
import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;

public class SempClientModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(ComponentCodeGenerator.class).to(JavassistComponentCodeGenerator.class);
		try {
			bind(EventComponents.class).toConstructor(EventComponents.class.getConstructor(ComponentCodeGenerator.class)).in(Scopes.SINGLETON);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("Could not bind "+EventComponents.class, e);
		}
	}
}
