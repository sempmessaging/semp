package net.davidtanzer.html;

import net.davidtanzer.html.values.AttributeName;
import net.davidtanzer.html.values.AttributeValue;
import net.davidtanzer.html.values.TagName;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EmptyElementTest {
	private EmptyElement element;

	@Before
	public void setup() {
		element = new EmptyElement(TagName.of("testElement")) {
		};
	}

	@Test
	public void rendersEmptyElementWithCloseTagWhenNoContentOrAttributesWereAdded() {
		String rendered = element.render();

		assertEquals("<testElement>", rendered);
	}

	@Test
	public void rendersElementWithAttributesWhenAttributesWereAdded() {
		element.setAttribute(AttributeName.of("attr1"), new AttributeValue("value1"));
		element.setAttribute(AttributeName.of("attr2"), new AttributeValue("value2"));

		String rendered = element.render();

		assertTrue(rendered.contains(" attr1=\"value1\""));
		assertTrue(rendered.contains(" attr2=\"value2\""));
	}
}