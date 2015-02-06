package net.davidtanzer.html.elements;

import net.davidtanzer.html.Element;
import net.davidtanzer.html.values.TagName;

public class Ul extends Element implements FlowContentNode, PalpableContentNode {
	public Ul() {
		super(TagName.of("ul"));
	}

	public void add(final Li... nodes) {
		super.add(nodes);
	}

	public void removeAllChildren() {
		super.removeAllChildren();
	}
}
