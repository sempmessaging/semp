package net.davidtanzer.html.elements;

import net.davidtanzer.html.Element;
import net.davidtanzer.html.values.TagName;

public class Strong extends Element implements FlowContentNode, PhrasingContentNode, PalpableContentNode {
	public Strong() {
		super(TagName.of("strong"));
	}

	public Strong add(final PhrasingContentNode... nodes) {
		super.add(nodes);
		return this;
	}
}
