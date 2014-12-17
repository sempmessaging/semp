package net.davidtanzer.jevents.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.davidtanzer.jevents.SomeComponent;
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
