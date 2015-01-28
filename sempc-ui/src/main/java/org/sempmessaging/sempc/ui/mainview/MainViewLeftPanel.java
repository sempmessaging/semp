package org.sempmessaging.sempc.ui.mainview;

import com.google.inject.Inject;
import net.davidtanzer.html.elements.FlowContentNode;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.conversations.ConversationsHeadline;
import org.sempmessaging.sempc.ui.menu.MainMenuButton;

public class MainViewLeftPanel extends MainViewPanel {
	private final MainMenuButton mainMenuButton;
	private final ConversationsHeadline conversationsHeadline;

	@Inject
	public MainViewLeftPanel(final MainMenuButton mainMenuButton, final ConversationsHeadline conversationsHeadline) {
		Args.notNull(mainMenuButton, "menuOpenButton");
		Args.notNull(conversationsHeadline, "conversationsHeadline");

		this.mainMenuButton = mainMenuButton;
		this.conversationsHeadline = conversationsHeadline;
	}

	@Override
	protected FlowContentNode[] headerItems() {
		return new FlowContentNode[] { mainMenuButton.getHtml(), conversationsHeadline.getHtml() };
	}
}
