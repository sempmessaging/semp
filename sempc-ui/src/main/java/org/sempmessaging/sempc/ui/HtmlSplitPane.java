package org.sempmessaging.sempc.ui;

import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.values.CssClass;
import net.davidtanzer.html.values.CssStyle;

public class HtmlSplitPane extends HtmlComponent {
	private int dividerLocation = 30;

	public HtmlSplitPane() {
		cssClass(new CssClass("split-pane"));
		cssClass(new CssClass("horizontal-split"));
	}

	@Override
	protected FlowContentNode[] getInnerHtml() {
		Div first = new Div();
		first.cssClass(new CssClass("first"));
		first.style(new CssStyle("width: "+dividerLocation+"%;"));

		Div second = new Div();
		second.cssClass(new CssClass("second"));
		second.style(new CssStyle("width: calc("+(100-dividerLocation)+"% - 2px);"));

		Div divider = new Div();
		divider.cssClass(new CssClass("divider"));

		return new FlowContentNode[] { first, divider, second };
	}
}
