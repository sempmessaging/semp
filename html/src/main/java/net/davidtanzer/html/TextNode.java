package net.davidtanzer.html;

import net.davidtanzer.value.StringValue;

public class TextNode implements Node {
	private final String text;

	public TextNode(final StringValue text) {
		//FIXME do some sanity checks on text: It must not contain HTML code!
		this.text = text.value();
	}

	@Override
	public void render(final StringBuilder renderedResultBuilder) {
		renderedResultBuilder.append(text);
	}
}
