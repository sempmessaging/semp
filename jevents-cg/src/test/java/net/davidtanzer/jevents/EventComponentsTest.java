package net.davidtanzer.jevents;

import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeNotNull;

public class EventComponentsTest {

	private final EventComponents eventComponents = new EventComponents(new JavassistComponentCodeGenerator());

	@Before
	public void setup() {
	}

	@Test
	public void eventComponentsCreatesNonNullInstanceOfComponent() {
		SomeComponent component = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(SomeComponent.class);

		assertNotNull(component);
	}

	@Test
	public void eventMethodOfComponentDoesNotReturnNull() {
		SomeComponent component = eventComponents.createComponent(SomeComponent.class);

		assumeNotNull(component);
		assertNotNull(component.doSomething());
	}
}
