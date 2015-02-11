package org.sempmessaging.sempc.ui.javafx;

import com.google.inject.Singleton;
import javafx.application.Platform;
import net.davidtanzer.html.Node;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.ComponentChangedListener;

@Singleton
public class ConversationsViewComponentChangedListener implements ComponentChangedListener {
	private ConversationsView conversationsView;

	@Override
	public void htmlComponentChanged(final String id, Node[] newContent) {
		Args.notEmpty(id, "id");
		Args.notNull(newContent, "newContent");

		assert conversationsView != null : "conversationsView must be set by dependency injection or test setup.";

		String contentString = render(newContent);
		String escapedContentString = escapeStrings(contentString);

		String script = "document.getElementById(\"" + id + "\").innerHTML=\"" + escapedContentString + "\";";
		//System.out.println("Running script in webengine: "+script);

		Platform.runLater(() -> {
			conversationsView.engine().executeScript(script);
		});
	}

	private String render(final Node[] nodes) {
		StringBuilder renderedResultBuilder = new StringBuilder();

		for(Node node : nodes) {
			node.render(renderedResultBuilder);
		}

		return renderedResultBuilder.toString();
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
