package org.sempmessaging.sempc.ui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.davidtanzer.html.elements.Html;
import net.davidtanzer.html.elements.values.CssLink;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.mainview.MainView;

public class MainHtmlPage {

	private MainView mainView;

	@Inject
	public MainHtmlPage(final MainView mainView) {
		Args.notNull(mainView, "mainView");
		this.mainView = mainView;
	}

	public Html getHtml() {
		Html rootNode = new Html();
		rootNode.head().addCssLink(new CssLink("res:///style/style.css"));

		rootNode.body().add(mainView.getHtml());

		return rootNode;
	}
}
