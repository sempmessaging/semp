package org.sempmessaging.sempc.ui.viewmodel;

import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractViewModelTest {
	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	private TestViewModel viewModel;

	@Before
	public void setup() throws Exception {
		viewModel = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(TestViewModel.class);
	}

	@Test
	public void sendsPropertyChangedEventWhenPropertyValueChanges() {
		eventTestRule.subscribeMandatory(viewModel, viewModel.propertyChangedEvent(),
				(property) -> assertEquals("testValue", property.get()));

		viewModel.stringProperty.set("testValue");
	}

	public static abstract class TestViewModel extends AbstractViewModel {
		public Property<String> stringProperty = newProperty(String.class);
	}
}