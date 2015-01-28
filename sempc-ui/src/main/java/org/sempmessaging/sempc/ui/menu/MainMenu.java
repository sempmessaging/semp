package org.sempmessaging.sempc.ui.menu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.values.CssClass;
import net.davidtanzer.jevents.Event;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.HtmlComponent;

public abstract class MainMenu extends HtmlComponent {
	private MainMenuButton mainMenuButton;
	private boolean showing;

	@Event
	public abstract ShowMainMenuEvent showMainMenuEvent();

	public MainMenu() {
		cssClass(new CssClass("menu-overlay"));
	}

	@Override
	protected FlowContentNode[] getInnerHtml() {
		return new FlowContentNode[0];
	}

	@Inject
	public void setMainMenuButton(final MainMenuButton mainMenuButton) {
		Args.notNull(mainMenuButton, "mainMenuButton");
		Args.setOnce(this.mainMenuButton, "mainMenuButton");

		mainMenuButton.subscribe(mainMenuButton.buttonClickedEvent(), () -> {
			this.showing = !this.showing;
			send(showMainMenuEvent()).showMainMenu(showing);
		});
		this.mainMenuButton = mainMenuButton;
	}
}
