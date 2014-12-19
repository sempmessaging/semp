package org.sempmessaging.sempc.ui.javafx;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.layout.BorderPane;
import org.sempmessaging.libsemp.arguments.Args;

@Singleton
public class MainView extends BorderPane {
	private ConversationsView conversationsView;

	@Inject
	public MainView(final ConversationsView conversationsView) {
		Args.notNull(conversationsView, "ConversationsView");

		this.conversationsView = conversationsView;

		initializeView();
	}

	private void initializeView() {
		setCenter(conversationsView.view());
	}
}
