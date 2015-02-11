package org.sempmessaging.sempc.ui.event;

import net.davidtanzer.html.values.EventHandlerScript;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventHandlerTemplateTest {
	private EventHandlerTemplate template;

	@Before
	public void setup() {
		template = new EventHandlerTemplate("test%s(1%s)");
	}

	@Test
	public void noParametersCreatesSimpleCall() {
		EventHandlerScript script = template.eventHandlerScript();

		assertEquals("test(1)", script.value());
	}

	@Test
	public void oneParameterCreatesParametrizedCalls() {
		EventHandlerScript script = template.eventHandlerScript("foo");

		assertEquals("test1(1, 'foo')", script.value());
	}

	@Test
	public void multipleParametersCreatesParametrizedCalls() {
		EventHandlerScript script = template.eventHandlerScript("foo", "bar");

		assertEquals("test2(1, 'foo', 'bar')", script.value());
	}
}