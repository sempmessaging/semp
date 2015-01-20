package org.sempmessaging.sempc.ui.javafx;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import net.davidtanzer.html.elements.Html;
import netscape.javascript.JSObject;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.ConversationsPanel;

@Singleton
public class ConversationsView {
	private final WebView webView = new WebView();

	@Inject
	public ConversationsView(final ConversationsPanel conversationsPanel, final ConversationsViewComponentChangedListener componentChangedListener, final JavaFxEventHandlerProvider eventHandlerProvider) {
		Args.notNull(conversationsPanel, "conversationsPanel");
		Args.notNull(componentChangedListener, "componentChangedListener");
		Args.notNull(eventHandlerProvider, "eventHandlerProvider");

		componentChangedListener.notifyOnChanges(this);

		Html html = conversationsPanel.getHtml();
		webView.getEngine().loadContent(html.render());

		JSObject jsobj = (JSObject) webView.getEngine().executeScript("window");
		jsobj.setMember("eventHandlerProvider", eventHandlerProvider);
	}

	Node view() {
		return webView;
	}

	WebEngine engine() {
		return webView.getEngine();
	}
}
