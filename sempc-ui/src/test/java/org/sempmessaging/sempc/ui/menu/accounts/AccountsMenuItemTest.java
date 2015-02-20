package org.sempmessaging.sempc.ui.menu.accounts;

import net.davidtanzer.html.Node;
import net.davidtanzer.html.TextNode;
import net.davidtanzer.html.elements.Headline;
import net.davidtanzer.html.elements.Strong;
import org.junit.Before;
import org.junit.Test;
import org.sempmessaging.sempc.core.account.*;
import org.sempmessaging.sempc.core.account.value.AccountName;
import org.sempmessaging.sempc.core.account.value.ConnectionStatus;
import org.sempmessaging.sempc.core.account.value.NumConversations;
import org.sempmessaging.sempc.core.account.value.NumUnreadConversations;

import java.util.List;

import static net.davidtanzer.html.query.HtmlQueries.select;
import static org.junit.Assert.assertEquals;

public class AccountsMenuItemTest {
	private AccountStatus accountStatus;
	private AccountsMenuItem accountsMenuItem;

	@Before
	public void setup() {
		accountStatus = new AccountStatus(ConnectionStatus.CONNECTING, new AccountName("Name"), new NumConversations(5), new NumUnreadConversations(2));
		accountsMenuItem = new AccountsMenuItem(accountStatus);
	}

	@Test
	public void showsAccountNameAsHeadline() {
		TextNode headline = (TextNode) select(Headline.class).select(TextNode.class).singleElement().from(accountsMenuItem.getHtml());

		assertEquals("Name", headline.render());
	}

	@Test
	public void showsTotalNumberOfConversations() {
		List<Node> nodes = select(Strong.class).select(TextNode.class).from(accountsMenuItem.getHtml());

		assertEquals(2, nodes.size());
		assertEquals("5", nodes.get(0).render());
	}

	@Test
	public void showsNumberOfUnreadConversations() {
		List<Node> nodes = select(Strong.class).select(TextNode.class).from(accountsMenuItem.getHtml());

		assertEquals(2, nodes.size());
		assertEquals("2", nodes.get(1).render());
	}
}