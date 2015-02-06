package net.davidtanzer.html.elements;

import net.davidtanzer.html.Element;
import net.davidtanzer.html.values.TagName;

public class Li extends Element {
	public Li() {
		super(TagName.of("div"));
	}

	public void add(final FlowContentNode... nodes) {
		super.add(nodes);
	}

	public void removeAllChildren() {
		super.removeAllChildren();
	}
}
