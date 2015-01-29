package org.sempmessaging.sempc.ui.components;

import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.values.CssClass;
import org.sempmessaging.sempc.ui.HtmlComponent;

public class HtmlSplitPane extends HtmlComponent {
	private Div first;
	private Div second;
	private Div divider;
	private HtmlComponent firstComponent;
	private HtmlComponent secondComponent;

	public HtmlSplitPane() {
		cssClass(new CssClass("split-pane"));
		cssClass(new CssClass("horizontal-split"));
	}

	@Override
	protected FlowContentNode[] getInnerHtml() {
		if(firstComponent != null) {
			first.removeAllChildren();
			first.add(firstComponent.getHtml());
		}

		if(secondComponent != null) {
			second.removeAllChildren();
			second.add(secondComponent.getHtml());
		}

		return new FlowContentNode[] { first, divider, second };
	}

	@Override
	protected void initializeComponent() {
		first = new Div();
		first.cssClass(new CssClass("first"));

		second = new Div();
		second.cssClass(new CssClass("second"));

		divider = new Div();
		divider.cssClasses(new CssClass("border-primary"), new CssClass("divider"));
	}

	public void firstComponent(final HtmlComponent component) {
		firstComponent = component;
	}

	public void secondComponent(final HtmlComponent component) {
		secondComponent = component;
	}
}
