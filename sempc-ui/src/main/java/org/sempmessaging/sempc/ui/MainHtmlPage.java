package org.sempmessaging.sempc.ui;

import com.google.inject.Inject;
import net.davidtanzer.html.elements.Html;
import net.davidtanzer.html.elements.values.CssLink;
import org.sempmessaging.sempc.ui.mainview.MainView;

public class MainHtmlPage {
	private final Html rootNode;

	@Inject
	public MainHtmlPage(final MainView mainView) {
		rootNode = new Html();
		rootNode.head().addCssLink(new CssLink("res:///style/style.css"));

		rootNode.body().add(mainView.getHtml());
	}

	public Html getHtml() {
		return rootNode;
	}
}
