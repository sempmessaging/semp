package org.sempmessaging.sempc.ui.menu.accounts;

import net.davidtanzer.html.TextNode;
import net.davidtanzer.html.elements.*;
import net.davidtanzer.html.elements.values.ImageSrc;
import net.davidtanzer.html.values.CssClass;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.core.account.AccountStatus;
import org.sempmessaging.sempc.core.account.value.ConnectionStatus;
import org.sempmessaging.sempc.ui.HtmlComponent;

import java.util.HashMap;
import java.util.Map;

public class AccountsMenuItem extends HtmlComponent {
	private static final Map<ConnectionStatus, ImageSrc> connectionStatusImages = new HashMap<ConnectionStatus, ImageSrc>() {{
		put(ConnectionStatus.UNKNOWN, new ImageSrc("res:///img/plug42.png"));
		put(ConnectionStatus.CONNECTING, new ImageSrc("res:///img/plug42_orange.png"));
		put(ConnectionStatus.ERROR, new ImageSrc("res:///img/plug42_red.png"));
		put(ConnectionStatus.CONNECTED, new ImageSrc("res:///img/plug42_green.png"));
	}};

	private AccountStatus accountStatus;
	private Div infoDiv;
	private Img connectionImage;

	public AccountsMenuItem(final AccountStatus accountStatus) {
		Args.notNull(accountStatus, "accountStatus");
		this.accountStatus = accountStatus;

		cssClass(new CssClass("accounts-menu-item"));
		cssClass(new CssClass("border-secondary"));
	}

	@Override
	protected FlowContentNode[] getInnerHtml() {
		return new FlowContentNode[] {connectionImage, infoDiv};
	}

	@Override
	protected void initializeComponent() {
		infoDiv = new Div();
		infoDiv.cssClass(new CssClass("account-info"));
		Headline accountName = new Headline(Headline.HeadlineLevel.H3);
		accountName.add(new TextNode(accountStatus.accountName()));
		infoDiv.add(accountName);
		infoDiv.add(new Strong().add(new TextNode(accountStatus.numConversations().value().toString())));
		infoDiv.add(new TextNode(" Conversations, "));
		infoDiv.add(new Strong().add(new TextNode(accountStatus.numUnreadConversations().value().toString())));
		infoDiv.add(new TextNode(" Unread"));

		connectionImage = new Img(connectionStatusImages.get(accountStatus.connectionStatus()));
		connectionImage.cssClasses(new CssClass("connection-status-icon"));
	}
}
