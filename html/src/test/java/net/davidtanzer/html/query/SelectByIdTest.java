package net.davidtanzer.html.query;

import net.davidtanzer.html.Node;
import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.values.Id;
import org.junit.Test;

import static net.davidtanzer.html.query.HtmlQueries.select;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assume.assumeNotNull;

public class SelectByIdTest {
	@Test
	public void queryByIdReturnsNonNullQueryObject() {
		SingleQuery query = select(new Id("testId"));
		assertNotNull(query);
	}

	@Test
	public void queryFromSingleMatchingNodeReturnsTheNode() {
		SingleQuery query = select(new Id("testId"));
		assumeNotNull(query);

		Div div = new Div();
		div.id(new Id("testId"));

		Node selectedNode = query.from(div);
		assertSame(div, selectedNode);
	}
}
