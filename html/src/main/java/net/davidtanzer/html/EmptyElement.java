package net.davidtanzer.html;

import net.davidtanzer.html.values.TagName;

public abstract class EmptyElement extends BaseElement implements Node {
	protected EmptyElement(final TagName tagName) {
		super(tagName);
	}

	@Override
	public String render() {
		StringBuilder renderedResultBuilder = new StringBuilder();
		render(renderedResultBuilder);
		return renderedResultBuilder.toString();
	}

	@Override
	public void render(final StringBuilder renderedResultBuilder) {
		renderOpenTag(renderedResultBuilder);
	}
}
