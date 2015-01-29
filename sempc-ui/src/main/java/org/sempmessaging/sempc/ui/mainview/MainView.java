package org.sempmessaging.sempc.ui.mainview;

import com.google.inject.Inject;
import net.davidtanzer.html.elements.FlowContentNode;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.HtmlComponent;
import org.sempmessaging.sempc.ui.components.HtmlSplitPane;
import org.sempmessaging.sempc.ui.menu.MainMenu;

public class MainView extends HtmlComponent {
	private final HtmlSplitPane splitPane;
	private final MainMenu mainMenu;
	private boolean showMenu;

	@Inject
	public MainView(final HtmlSplitPane splitPane, final MainViewLeftPanel leftPanel, final MainViewRightPanel rightPanel, final MainMenu mainMenu) {
		Args.notNull(splitPane, "splitPane");
		Args.notNull(leftPanel, "leftPanel");
		Args.notNull(rightPanel, "rightPanel");
		Args.notNull(mainMenu, "mainMenu");

		splitPane.firstComponent(leftPanel);
		splitPane.secondComponent(rightPanel);

		mainMenu.subscribe(mainMenu.showMainMenuEvent(), this::showMainMenu);

		this.splitPane = splitPane;
		this.mainMenu = mainMenu;
	}

	@Override
	protected FlowContentNode[] getInnerHtml() {
		if(showMenu) {
			return new FlowContentNode[] { mainMenu.getHtml(), splitPane.getHtml() };
		}
		return new FlowContentNode[] { splitPane.getHtml() };
	}

	private void showMainMenu(final boolean showMenu) {
		this.showMenu = showMenu;
		componentChanged();
	}
}
