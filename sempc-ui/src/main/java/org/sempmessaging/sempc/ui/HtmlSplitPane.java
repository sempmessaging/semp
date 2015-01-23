package org.sempmessaging.sempc.ui;

import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.values.CssClass;
import net.davidtanzer.html.values.CssStyle;

public class HtmlSplitPane extends HtmlComponent {
	public HtmlSplitPane() {
		cssClass(new CssClass("split-pane"));
		cssClass(new CssClass("horizontal-split"));
	}

	@Override
	protected FlowContentNode[] getInnerHtml() {
		Div first = new Div();
		first.cssClass(new CssClass("first"));

		Div second = new Div();
		second.cssClass(new CssClass("second"));

		Div divider = new Div();
		divider.cssClass(new CssClass("divider"));

		return new FlowContentNode[] { first, divider, second };
	}
}
