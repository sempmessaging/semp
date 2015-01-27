package org.sempmessaging.sempc.ui;

import net.davidtanzer.html.Node;
import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.values.AttributeName;
import net.davidtanzer.html.values.CssClass;
import net.davidtanzer.html.values.EventHandlerScript;
import net.davidtanzer.html.values.Id;
import org.junit.Test;
import org.sempmessaging.sempc.ui.event.EventHandlerProvider;

import java.util.List;

import static net.davidtanzer.html.query.HtmlQueries.selectAttribute;
import static net.davidtanzer.html.query.HtmlQueries.selectChildren;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HtmlComponentTest {
	@Test
	public void rootNodeOfAnHtmlComponentIsADiv() {
		HtmlComponent htmlComponent = new TestHtmlComponent();

		assertEquals(Div.class, htmlComponent.getHtml().getClass());
	}

	@Test
	public void everyHtmlComponentHasAnId() {
		HtmlComponent htmlComponent = new TestHtmlComponent();

		Id id = (Id) selectAttribute(AttributeName.of("id")).from(htmlComponent.getHtml());

		assertNotNull(id);
	}

	@Test
	public void everyHtmlComponentHasADifferentId() {
		Id id1 = (Id) selectAttribute(AttributeName.of("id")).from(new TestHtmlComponent().getHtml());
		Id id2 = (Id) selectAttribute(AttributeName.of("id")).from(new TestHtmlComponent().getHtml());

		assumeNotNull(id1);
		assumeNotNull(id2);

		assertNotEquals(id1, id2);
	}

	@Test
	public void outerDivOfHtmlComponentHasAllCssClassesOfTheComponent() {
		HtmlComponent htmlComponent = new TestHtmlComponent(new CssClass("foo"), new CssClass("bar"));

		CssClass cssClass = (CssClass) selectAttribute(AttributeName.of("class")).from(htmlComponent.getHtml());

		assertEquals("foo bar", cssClass.value());
	}

	@Test
	public void htmlComponentContainsInnerElements() {
		HtmlComponent htmlComponent = new TestHtmlComponent();

		List<Node> children = selectChildren().from(htmlComponent.getHtml());

		assertEquals(1, children.size());
		assertEquals(Div.class, children.get(0).getClass());

		Id id = (Id) selectChildren().selectAttribute(AttributeName.of("id")).from(htmlComponent.getHtml());
		assertEquals(new Id("inner"), id);
	}

	@Test
	public void notifiesComponentChangedListenerWhenComponentChanges() {
		HtmlComponent htmlComponent = new TestHtmlComponent();
		ComponentChangedListener ccl = mock(ComponentChangedListener.class);
		htmlComponent.setComponentChangedListener(ccl);

		htmlComponent.componentChanged();

		verify(ccl).htmlComponentChanged(anyString(), any());
	}

	@Test
	public void usesEventHandlerProviderToCreateEventJavaScript() {
		HtmlComponent htmlComponent = new TestHtmlComponent();
		EventHandlerProvider ehp = mock(EventHandlerProvider.class);
		when(ehp.provideEventHandler(any())).thenReturn(new EventHandlerScript("script"));
		htmlComponent.setEventHandlerProvider(ehp);

		EventHandlerScript eventHandlerScript = htmlComponent.eventHandler(() -> {
		});

		assertEquals(new EventHandlerScript("script"), eventHandlerScript);
	}

	private static class TestHtmlComponent extends HtmlComponent {
		private Div div;

		public TestHtmlComponent() {
		}

		public TestHtmlComponent(final CssClass... cssClasses) {
			for(CssClass c : cssClasses) {
				cssClass(c);
			}
		}

		@Override
		protected FlowContentNode[] getInnerHtml() {
			return new FlowContentNode[] {div};
		}

		@Override
		protected void initializeComponent() {
			div = new Div();
			div.id(new Id("inner"));
		}
	}
}