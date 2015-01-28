package org.sempmessaging.sempc.ui.viewmodel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PropertyTest {
	private AbstractViewModel owner;
	private Property<String> property;

	@Before
	public void setup() {
		owner = mock(AbstractViewModel.class);
		property = new Property<String>(owner);
	}

	@Test
	public void setChangesThePropertyValue() {
		property.set("testValue");

		assertEquals("testValue", property.get());
	}

	@Test
	public void setNotifiesOwnerAboutNewValue() {
		property.set("testValue");

		verify(owner).propertyChanged(eq(property));
	}
}