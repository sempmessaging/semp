package net.davidtanzer.html;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ElementTest {
	private Element element;

	@Before
	public void setup() {
		element = new Element(TagName.of("testElement")) {
		};
	}

	@Test
	public void rendersEmptyElementWithCloseTagWhenNoContentOrAttributesWereAdded() {
		String rendered = element.render();

		assertEquals("<testElement></testElement>", rendered);
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