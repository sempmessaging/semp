package net.davidtanzer.html.query;

import net.davidtanzer.html.Node;
import net.davidtanzer.html.elements.Div;
import org.junit.Test;

import java.util.List;

import static net.davidtanzer.html.query.HtmlQueries.selectChildren;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assume.assumeNotNull;

public class SelectChildrenTest {
	@Test
	public void returnsEmptyListWhenNoChildrenExist() {
		List<Node> nodes = selectChildren().from(new Div());

		assertNotNull(nodes);
		assertEquals(0, nodes.size());
	}

	@Test
	public void returnsListOfChildrenWhenChildrenExist() {
		Div root = new Div();
		Div child1 = new Div();
		Div child2 = new Div();
		root.add(child1, child2);

		List<Node> nodes = selectChildren().from(root);

		assumeNotNull(nodes);
		assertEquals(2, nodes.size());
		assertSame(child1, nodes.get(0));
		assertSame(child2, nodes.get(1));
	}

	@Test
	public void doesNotSelectGrandChildren() {
		Div root = new Div();
		Div child = new Div();
		Div grandChild = new Div();
		child.add(grandChild);
		root.add(child);

		List<Node> nodes = selectChildren().from(root);

		assumeNotNull(nodes);
		assertEquals(1, nodes.size());
		assertSame(child, nodes.get(0));
	}
}
