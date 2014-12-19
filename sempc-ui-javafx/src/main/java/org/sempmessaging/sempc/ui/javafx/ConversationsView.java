package org.sempmessaging.sempc.ui.javafx;

import com.google.inject.Inject;
import javafx.scene.Node;
import javafx.scene.web.WebView;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.ConversationsPanel;

public class ConversationsView {
	private final WebView webView = new WebView();
	private ConversationsPanel conversationsPanel;

	@Inject
	public ConversationsView(final ConversationsPanel conversationsPanel) {
		Args.notNull(conversationsPanel, "conversationsPanel");

		this.conversationsPanel = conversationsPanel;

		webView.getEngine().loadContent(conversationsPanel.getHtml());
	}

	public Node view() {
		return webView;
	}
}
