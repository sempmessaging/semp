package org.sempmessaging.sempc.ui.mainview;

import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.values.CssClass;
import org.sempmessaging.sempc.ui.HtmlComponent;

public class MainViewPanel extends HtmlComponent {
	private Div header;
	private Div content;

	@Override
	protected FlowContentNode[] getInnerHtml() {
		header.removeAllChildren();
		header.add(headerItems());

		content.removeAllChildren();
		content.add(contentItems());

		return new FlowContentNode[] {header, content};
	}

	@Override
	protected void initializeComponent() {
		header = new Div();
		header.cssClasses(new CssClass("border-primary"), new CssClass("background-secondary"), new CssClass("panel-header"));

		content = new Div();
		content.cssClass(new CssClass("panel-content"));
	}

	protected FlowContentNode[] headerItems() {
		return new FlowContentNode[0];
	}

	protected FlowContentNode[] contentItems() {
		return new FlowContentNode[0];
	}
}
