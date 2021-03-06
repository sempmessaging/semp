package net.davidtanzer.html.elements;

import net.davidtanzer.html.Element;
import net.davidtanzer.html.values.TagName;

public class Body extends Element {
	Body() {
		super(TagName.of("body"));
	}

	public void add(final FlowContentNode node) {
		super.add(node);
	}
}
