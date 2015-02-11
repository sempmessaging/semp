package org.sempmessaging.sempc.ui.menu;

import com.google.inject.Inject;
import net.davidtanzer.html.TextNode;
import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.elements.Headline;
import net.davidtanzer.html.values.CssClass;
import net.davidtanzer.jevents.Event;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.HtmlComponent;
import org.sempmessaging.sempc.ui.connection.ConnectionStatusPanel;
import org.sempmessaging.sempc.ui.menu.accounts.AccountsMenuComponent;

public abstract class MainMenu extends HtmlComponent {
	private MainMenuButton mainMenuButton;

	private Div menuContainerDiv;
	private MainMenuCloseButton mainMenuCloseButton;
	private AccountsMenuComponent accountsMenuComponent;

	@Event
	public abstract ShowMainMenuEvent showMainMenuEvent();

	public MainMenu() {
		cssClass(new CssClass("menu-overlay"));
	}

	@Override
	protected FlowContentNode[] getInnerHtml() {
		return new FlowContentNode[] { menuContainerDiv };
	}

	@Override
	protected void initializeComponent() {
		menuContainerDiv = new Div();
		menuContainerDiv.cssClasses(new CssClass("menu-container"), new CssClass("border-primary"));

		Div header = new Div();
		header.cssClasses(new CssClass("border-primary"), new CssClass("background-secondary"), new CssClass("panel-header"));
		header.add(mainMenuCloseButton.getHtml());

		Headline accountsHeadline = new Headline(Headline.HeadlineLevel.H2);
		accountsHeadline.add(new TextNode("Accounts"));
		header.add(accountsHeadline);

		Div content = new Div();
		content.cssClasses(new CssClass("panel-content"), new CssClass("background-primary"));

		content.add(accountsMenuComponent.getHtml());

		menuContainerDiv.add(header, content);
	}

	@Inject
	public void setAccountsMenuComponent(final AccountsMenuComponent accountsMenuComponent) {
		Args.notNull(accountsMenuComponent, "accountsMenuComponent");
		Args.setOnce(this.accountsMenuComponent, "accountsMenuComponent");

		this.accountsMenuComponent = accountsMenuComponent;
	}

	@Inject
	public void setMainMenuCloseButton(final MainMenuCloseButton mainMenuCloseButton) {
		Args.notNull(mainMenuCloseButton, "mainMenuCloseButton");
		Args.setOnce(this.mainMenuCloseButton, "mainMenuCloseButton");

		mainMenuCloseButton.subscribe(mainMenuCloseButton.buttonClickedEvent(), () -> send(showMainMenuEvent()).showMainMenu(false));
		this.mainMenuCloseButton = mainMenuCloseButton;
	}

	@Inject
	public void setMainMenuButton(final MainMenuButton mainMenuButton) {
		Args.notNull(mainMenuButton, "mainMenuButton");
		Args.setOnce(this.mainMenuButton, "mainMenuButton");

		mainMenuButton.subscribe(mainMenuButton.buttonClickedEvent(), () -> send(showMainMenuEvent()).showMainMenu(true));
		this.mainMenuButton = mainMenuButton;
	}
}
