package net.davidtanzer.html;

import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.value.StringValue;

public class TextNode implements FlowContentNode {
	private final String text;

	public TextNode(final StringValue text) {
		this(text.value());
	}

	public TextNode(final String text) {
		//FIXME do some sanity checks on text: It must not contain HTML code!
		this.text = text;
	}

	@Override
	public void render(final StringBuilder renderedResultBuilder) {
		renderedResultBuilder.append(text);
	}
}
