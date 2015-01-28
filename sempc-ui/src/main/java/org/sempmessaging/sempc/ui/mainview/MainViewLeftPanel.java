package org.sempmessaging.sempc.ui.mainview;

import com.google.inject.Inject;
import net.davidtanzer.html.elements.FlowContentNode;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.conversations.ConversationsHeadline;
import org.sempmessaging.sempc.ui.menu.MenuOpenButton;

public class MainViewLeftPanel extends MainViewPanel {
	private final MenuOpenButton menuOpenButton;
	private final ConversationsHeadline conversationsHeadline;

	@Inject
	public MainViewLeftPanel(final MenuOpenButton menuOpenButton, final ConversationsHeadline conversationsHeadline) {
		Args.notNull(menuOpenButton, "menuOpenButton");
		Args.notNull(conversationsHeadline, "conversationsHeadline");

		this.menuOpenButton = menuOpenButton;
		this.conversationsHeadline = conversationsHeadline;
	}

	@Override
	protected FlowContentNode[] headerItems() {
		return new FlowContentNode[] { menuOpenButton.getHtml(), conversationsHeadline.getHtml() };
	}
}
