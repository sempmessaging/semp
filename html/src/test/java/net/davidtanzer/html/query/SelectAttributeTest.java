package net.davidtanzer.html.query;

import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.values.AttributeName;
import net.davidtanzer.html.values.AttributeValue;
import net.davidtanzer.html.values.Id;
import org.junit.Test;

import static net.davidtanzer.html.query.HtmlQueries.selectAttribute;
import static org.junit.Assert.assertEquals;

public class SelectAttributeTest {
	@Test
	public void selectAttributeFromElementReturnsTheAttributeValue() {
		Div element = new Div();
		element.id(new Id("test_id"));

		AttributeValue value = selectAttribute(AttributeName.of("id")).from(element);

		assertEquals(new Id("test_id"), value);
	}
}
