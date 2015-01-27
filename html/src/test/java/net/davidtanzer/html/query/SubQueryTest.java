package net.davidtanzer.html.query;

import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.values.AttributeName;
import net.davidtanzer.html.values.Id;
import org.junit.Test;

import static net.davidtanzer.html.query.HtmlQueries.select;
import static net.davidtanzer.html.query.HtmlQueries.selectAll;
import static org.junit.Assert.assertEquals;

public class SubQueryTest {
	@Test
	public void selectAttributeFromSingleQuery() {
		Div div = new Div();
		div.id(new Id("testId"));

		Id id = (Id) select(new Id("testId")).selectAttribute(AttributeName.of("id")).from(div);

		assertEquals(new Id("testId"), id);
	}

	@Test
	public void selectAttributeFromQuery() {
		Div div = new Div();
		div.id(new Id("testId"));

		Id id = (Id) selectAll().selectAttribute(AttributeName.of("id")).from(div);

		assertEquals(new Id("testId"), id);
	}
}
