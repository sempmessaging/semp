package org.sempmessaging.sempc.ui;

import net.davidtanzer.html.elements.Html;
import net.davidtanzer.html.elements.values.CssLink;

public class ConversationsPanel {
	private final Html rootNode;

	public ConversationsPanel() {
		rootNode = new Html();
		rootNode.head().addCssLink(new CssLink("res:///style/style.css"));

		HtmlSplitPane splitPane = new HtmlSplitPane();
		rootNode.body().add(splitPane.getHtml());
	}

	public Html getHtml() {
		return rootNode;
	}
}
