package org.sempmessaging.sempc.ui.javafx;

import com.google.inject.Singleton;
import javafx.application.Platform;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.ComponentChangedListener;

@Singleton
public class ConversationsViewComponentChangedListener implements ComponentChangedListener {
	private ConversationsView conversationsView;

	@Override
	public void htmlComponentChanged(final String id, String newContent) {
		assert conversationsView != null : "conversationsView must be set by dependency injection or test setup.";
		Platform.runLater(() ->
			conversationsView.engine().executeScript("document.getElementById(\"" + id + "\").innerHTML=\"" + escapeStrings(newContent) + "\";"));
	}

	private String escapeStrings(final String newContent) {
		return newContent.replaceAll("\"", "\\\\\"");
	}

	public void notifyOnChanges(final ConversationsView conversationsView) {
		Args.notNull(conversationsView, "conversationsView");
		Args.setOnce(this.conversationsView, "conversationsView");

		this.conversationsView = conversationsView;
	}
}
