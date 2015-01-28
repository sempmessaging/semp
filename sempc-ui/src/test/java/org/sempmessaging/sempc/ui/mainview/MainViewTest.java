package org.sempmessaging.sempc.ui.mainview;

import net.davidtanzer.html.Node;
import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.values.Id;
import org.junit.Before;
import org.junit.Test;
import org.sempmessaging.sempc.ui.HtmlSplitPane;
import org.sempmessaging.sempc.ui.menu.MainMenu;

import static net.davidtanzer.html.query.HtmlQueries.select;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainViewTest {
	private MainView mainView;
	private HtmlSplitPane splitPane;
	private MainViewLeftPanel leftPanel;
	private MainViewRightPanel rightPanel;
	private MainMenu mainMenu;

	@Before
	public void setup() {
		splitPane = mock(HtmlSplitPane.class);
		Div splitPaneDiv = new Div();
		splitPaneDiv.id(new Id("splitPane"));
		when(splitPane.getHtml()).thenReturn(splitPaneDiv);

		leftPanel = mock(MainViewLeftPanel.class);
		rightPanel = mock(MainViewRightPanel.class);

		mainMenu = mock(MainMenu.class);
		Div mainMenuDiv = new Div();
		mainMenuDiv.id(new Id("mainMenu"));
		when(mainMenu.getHtml()).thenReturn(mainMenuDiv);

		mainView = new MainView(splitPane, leftPanel, rightPanel, mainMenu);
	}

	@Test
	public void doesNotShowMainMenuByDefault() {
		Node mainMenuDiv = select(new Id("mainMenu")).from(mainView.getHtml());

		assertNull(mainMenuDiv);
	}

	@Test
	public void showsSplitPaneByDefault() {
		Node splitPaneDiv = select(new Id("splitPane")).from(mainView.getHtml());

		assertNotNull(splitPaneDiv);
	}
}