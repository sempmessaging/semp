package net.davidtanzer.html.elements;

import net.davidtanzer.html.Element;
import net.davidtanzer.html.values.TagName;

public class Div extends Element implements FlowContentNode {
	public Div() {
		super(TagName.of("div"));
	}

	public void add(final FlowContentNode node) {
		super.add(node);
	}
}
