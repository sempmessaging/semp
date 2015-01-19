package net.davidtanzer.jevents.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import net.davidtanzer.jevents.ComponentCodeGenerator;
import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.SomeComponent;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class GuiceBindingTest {
	private Injector injector;

	@Before
	public void setup() {
		injector = Guice.createInjector(new EventComponentModule() {
			@Override
			protected void configure() {
				bind(ComponentCodeGenerator.class).to(JavassistComponentCodeGenerator.class);
				try {
					bind(EventComponents.class).toConstructor(EventComponents.class.getConstructor(ComponentCodeGenerator.class)).in(Scopes.SINGLETON);
				} catch (NoSuchMethodException e) {
					throw new IllegalStateException("Could not bind "+EventComponents.class, e);
				}

				bindEventComponent(SomeComponent.class);
			}
		});
	}

	@Test
	public void getSomeComponentFromGuice() {
		SomeComponent component = injector.getInstance(SomeComponent.class);
		assertNotNull(component);
	}
}
