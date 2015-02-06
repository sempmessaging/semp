package org.sempmessaging.sempc.ui.components;

import net.davidtanzer.html.BaseElement;
import net.davidtanzer.html.Node;
import net.davidtanzer.html.TextNode;
import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.elements.Li;
import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.sempmessaging.sempc.ui.HtmlComponent;
import org.sempmessaging.sempc.ui.JsEventTest;
import org.sempmessaging.sempc.ui.viewmodel.Property;

import java.util.Arrays;
import java.util.Collections;

import static net.davidtanzer.html.query.HtmlQueries.select;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListTest extends JsEventTest {
	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	private Property<java.util.List<String>> listContent;
	private ListItemRenderer<String> listItemRenderer;
	private List<String> list;

	@Before
	public void setup() throws Exception {
		listContent = mock(Property.class);
		listItemRenderer = mock(ListItemRenderer.class);

		list = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(List.class);
		list.setListContent(listContent);
		list.setListItemRenderer(listItemRenderer);
		list.setEventHandlerProvider(eventHandlerProvider());
	}

	@Test
	public void rendersNoListItemsWhenContentIsEmpty() {
		when(listContent.get()).thenReturn(Collections.<String>emptyList());

		java.util.List<Node> listItems = select(Li.class).from(list.getHtml());

		assertTrue(listItems.isEmpty());
	}

	@Test
	public void rendersLiTagForEachContentItem() {
		when(listContent.get()).thenReturn(Arrays.asList("foo", "bar"));
		when(listItemRenderer.render(anyString())).thenReturn(new HtmlComponent() {
			@Override
			protected FlowContentNode[] getInnerHtml() {
				return new FlowContentNode[] {new TextNode("")};
			}
		});

		java.util.List<Node> listItems = select(Li.class).from(list.getHtml());

		assertEquals(2, listItems.size());
	}

	@Test
	public void usesListItemRendererToRenderContentItems() {
		when(listContent.get()).thenReturn(Arrays.asList("foo"));
		when(listItemRenderer.render(anyString())).thenReturn(new HtmlComponent() {
			@Override
			protected FlowContentNode[] getInnerHtml() {
				return new FlowContentNode[] {new TextNode("test node")};
			}
		});

		java.util.List<Node> listItems = select(Li.class).from(list.getHtml());
		assumeThat(listItems.size(), is(1));
		Node text = select(TextNode.class).singleElement().from(listItems.toArray(new Node[listItems.size()]));

		assertEquals("test node", text.render());
	}

	@Test
	public void notifiesListenersWhenListItemsAreSelected() {
		when(listContent.get()).thenReturn(Arrays.asList("foo", "bar"));
		when(listItemRenderer.render(anyString())).thenReturn(new HtmlComponent() {
			@Override
			protected FlowContentNode[] getInnerHtml() {
				return new FlowContentNode[] {new TextNode("")};
			}
		});

		java.util.List<Node> listItems = select(Li.class).from(list.getHtml());
		assumeThat(listItems.size(), is(2));

		eventTestRule.subscribeMandatory(list, list.itemSelectedEvent(), (item) -> assertEquals("bar", item));
		click((BaseElement) listItems.get(1));
	}
}
