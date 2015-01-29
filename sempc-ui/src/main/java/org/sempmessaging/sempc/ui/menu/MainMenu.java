package org.sempmessaging.sempc.ui.menu;

import com.google.inject.Inject;
import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.values.CssClass;
import net.davidtanzer.jevents.Event;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.HtmlComponent;
import org.sempmessaging.sempc.ui.connection.ConnectionStatusPanel;

public abstract class MainMenu extends HtmlComponent {
	private MainMenuButton mainMenuButton;
	private ConnectionStatusPanel connectionStatusPanel;

	private Div menuContainerDiv;
	private MainMenuCloseButton mainMenuCloseButton;

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

		Div content = new Div();
		content.cssClasses(new CssClass("panel-content"), new CssClass("background-primary"));

		menuContainerDiv.add(header, content);
	}

	@Inject
	public void setMainMenuCloseButton(final MainMenuCloseButton mainMenuCloseButton) {
		Args.notNull(mainMenuCloseButton, "mainMenuCloseButton");
		Args.setOnce(this.mainMenuCloseButton, "mainMenuCloseButton");

		mainMenuCloseButton.subscribe(mainMenuCloseButton.buttonClickedEvent(), () -> send(showMainMenuEvent()).showMainMenu(false));
		this.mainMenuCloseButton = mainMenuCloseButton;
	}

	@Inject
	public void setConnectionStatusPanel(final ConnectionStatusPanel connectionStatusPanel) {
		Args.notNull(connectionStatusPanel, "connectionStatusPanel");
		Args.setOnce(this.connectionStatusPanel, "connectionStatusPanel");

		this.connectionStatusPanel = connectionStatusPanel;
	}

	@Inject
	public void setMainMenuButton(final MainMenuButton mainMenuButton) {
		Args.notNull(mainMenuButton, "mainMenuButton");
		Args.setOnce(this.mainMenuButton, "mainMenuButton");

		mainMenuButton.subscribe(mainMenuButton.buttonClickedEvent(), () -> send(showMainMenuEvent()).showMainMenu(true));
		this.mainMenuButton = mainMenuButton;
	}
}
