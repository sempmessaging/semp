package net.davidtanzer.html.elements;

import net.davidtanzer.html.EmptyElement;
import net.davidtanzer.html.values.TagName;

public class Br extends EmptyElement implements FlowContentNode, PhrasingContentNode {
	public Br() {
		super(TagName.of("br"));
	}
}
