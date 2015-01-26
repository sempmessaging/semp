package org.sempmessaging.sempc.ui;

import com.google.inject.Inject;
import net.davidtanzer.html.elements.Html;
import net.davidtanzer.html.elements.values.CssLink;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.connection.ConnectionStatusPanel;

public class ConversationsPanel {
	private final Html rootNode;

	@Inject
	public ConversationsPanel(final HtmlSplitPane splitPane, final ConnectionStatusPanel connectionStatusPanel, final MainViewLeftPanel leftPanel, final MainViewRightPanel rightPanel) {
		Args.notNull(splitPane, "splitPane");
		Args.notNull(connectionStatusPanel, "connectionStatusPanel");
		Args.notNull(leftPanel, "leftPanel");
		Args.notNull(rightPanel, "rightPanel");

		rootNode = new Html();
		rootNode.head().addCssLink(new CssLink("res:///style/style.css"));

		splitPane.firstComponent(leftPanel);
		splitPane.secondComponent(rightPanel);

		rootNode.body().add(splitPane.getHtml());
		rootNode.body().add(connectionStatusPanel.getHtml());
	}

	public Html getHtml() {
		return rootNode;
	}
}
