package org.sempmessaging.sempc.ui;

import net.davidtanzer.html.Node;
import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.values.CssClass;
import net.davidtanzer.html.values.Id;
import org.junit.Before;
import org.junit.Test;

import static net.davidtanzer.html.query.HtmlQueries.select;
import static org.junit.Assert.*;

public class HtmlSplitPaneTest {
	private HtmlSplitPane splitPane;

	@Before
	public void setup() {
		splitPane = new HtmlSplitPane();
	}

	@Test
	public void containsFirstComponentInFirstDiv() {
		final Div div = new Div();
		div.id(new Id("testId"));

		HtmlComponent firstComponent = new HtmlComponent() {
			@Override
			protected FlowContentNode[] getInnerHtml() {
				return new FlowContentNode[] { div };
			}
		};
		splitPane.firstComponent(firstComponent);

		Node node = select(new CssClass("first")).select(new Id("testId")).from(splitPane.getHtml());

		assertNotNull(node);
		assertEquals(div, node);
	}

	@Test
	public void containsSecondComponentInSecondDiv() {
		final Div div = new Div();
		div.id(new Id("testId"));

		HtmlComponent secondComponent = new HtmlComponent() {
			@Override
			protected FlowContentNode[] getInnerHtml() {
				return new FlowContentNode[] { div };
			}
		};
		splitPane.secondComponent(secondComponent);

		Node node = select(new CssClass("second")).select(new Id("testId")).from(splitPane.getHtml());

		assertNotNull(node);
		assertEquals(div, node);
	}
}