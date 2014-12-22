package org.sempmessaging.sempc.ui;

import net.davidtanzer.html.TextNode;
import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.elements.Html;
import net.davidtanzer.html.elements.values.CssLink;

public class ConversationsPanel {
	private final Html rootNode = new Html();

	public ConversationsPanel() {
		Div rootDiv = new Div();
		rootDiv.add(new TextNode("Hello World!"));

		rootNode.body().add(rootDiv);
		rootNode.head().addCssLink(new CssLink("res:///style/style.css"));
	}

	public Html getHtml() {
		return rootNode;
	}
}
