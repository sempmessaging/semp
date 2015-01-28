package net.davidtanzer.html.query;

import net.davidtanzer.html.Node;
import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.values.CssClass;
import org.junit.Test;

import java.util.List;

import static net.davidtanzer.html.query.HtmlQueries.select;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class SelectByClassTest {
	@Test
	public void selectsDivsWithGivenClassRegardlessOfStructure() {
		Div root = new Div();

		Div childWithClass = new Div();
		childWithClass.cssClasses(new CssClass("testClass"));
		root.add(childWithClass);

		Div childWithoutClass = new Div();
		root.add(childWithoutClass);
		Div grandChildWithClass = new Div();
		grandChildWithClass.cssClasses(new CssClass("testClass"));
		childWithoutClass.add(grandChildWithClass);

		List<Node> result = select(new CssClass("testClass")).from(root);

		assertNotNull(result);
		assertEquals(result.size(), 2);
		assertThat(result, hasItems(childWithClass, grandChildWithClass));
	}
}
