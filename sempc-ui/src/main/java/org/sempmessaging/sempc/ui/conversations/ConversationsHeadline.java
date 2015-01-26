package org.sempmessaging.sempc.ui.conversations;

import net.davidtanzer.html.TextNode;
import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.elements.Headline;
import net.davidtanzer.html.values.CssClass;
import org.sempmessaging.sempc.ui.HtmlComponent;

public class ConversationsHeadline extends HtmlComponent {
	private Headline headline;

	public ConversationsHeadline() {
		cssClass(new CssClass("panel-header-component"));
		cssClass(new CssClass("panel-header-headline"));
	}

	@Override
	protected void initializeComponent() {
		headline = new Headline(Headline.HeadlineLevel.H2);
		headline.add(new TextNode("All Conversations"));
	}

	@Override
	protected FlowContentNode[] getInnerHtml() {
		return new FlowContentNode[] { headline };
	}
}
