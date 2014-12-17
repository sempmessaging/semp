package net.davidtanzer.jevents;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeNotNull;

public class EventComponentsTest {

	@Before
	public void setup() {
	}

	@Test
	public void eventComponentsCreatesNonNullInstanceOfComponent() {
		SomeComponent component = EventComponents.createComponent(SomeComponent.class);

		assertNotNull(component);
	}

	@Test
	public void eventMethodOfComponentDoesNotReturnNull() {
		SomeComponent component = EventComponents.createComponent(SomeComponent.class);

		assumeNotNull(component);
		assertNotNull(component.doSomething());
	}
}
