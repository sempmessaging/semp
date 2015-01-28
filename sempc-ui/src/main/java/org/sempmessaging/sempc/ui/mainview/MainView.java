package org.sempmessaging.sempc.ui.mainview;

import com.google.inject.Inject;
import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.elements.FlowContentNode;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.HtmlComponent;
import org.sempmessaging.sempc.ui.HtmlSplitPane;
import org.sempmessaging.sempc.ui.menu.MainMenu;

public class MainView extends HtmlComponent {
	private final HtmlSplitPane splitPane;
	private Div containerDiv;

	@Inject
	public MainView(final HtmlSplitPane splitPane, final MainViewLeftPanel leftPanel, final MainViewRightPanel rightPanel, final MainMenu mainMenu) {
		Args.notNull(splitPane, "splitPane");
		Args.notNull(leftPanel, "leftPanel");
		Args.notNull(rightPanel, "rightPanel");
		Args.notNull(mainMenu, "mainMenu");

		splitPane.firstComponent(leftPanel);
		splitPane.secondComponent(rightPanel);

		this.splitPane = splitPane;
	}

	@Override
	protected FlowContentNode[] getInnerHtml() {
		containerDiv.removeAllChildren();
		containerDiv.add(splitPane.getHtml());

		return new FlowContentNode[] { containerDiv };
	}

	@Override
	protected void initializeComponent() {
		containerDiv = new Div();
	}
}
