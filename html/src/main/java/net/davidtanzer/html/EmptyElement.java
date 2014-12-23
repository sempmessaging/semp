package net.davidtanzer.html;

import net.davidtanzer.html.values.TagName;

public abstract class EmptyElement extends BaseElement implements Node {
	protected EmptyElement(final TagName tagName) {
		super(tagName);
	}

	@Override
	public void render(final StringBuilder renderedResultBuilder) {
		renderOpenTag(renderedResultBuilder);
	}
}
