package net.davidtanzer.html.elements;

import net.davidtanzer.html.Element;
import net.davidtanzer.html.values.TagName;
import net.davidtanzer.html.TextNode;
import net.davidtanzer.html.elements.values.PageTitle;

public class Title extends Element {
	protected Title(final PageTitle title) {
		super(TagName.of("title"));
		add(new TextNode(title));
	}
}
