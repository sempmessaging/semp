package org.sempmessaging.sempc.ui;

import com.google.inject.Inject;
import net.davidtanzer.html.elements.Html;
import net.davidtanzer.html.elements.values.CssLink;

public class ConversationsPanel {
	private final Html rootNode;

	@Inject
	public ConversationsPanel(final HtmlSplitPane splitPane, final ConnectionStatusPanel connectionStatusPanel) {
		rootNode = new Html();
		rootNode.head().addCssLink(new CssLink("res:///style/style.css"));

		rootNode.body().add(splitPane.getHtml());
		rootNode.body().add(connectionStatusPanel.getHtml());
	}

	public Html getHtml() {
		return rootNode;
	}
}
