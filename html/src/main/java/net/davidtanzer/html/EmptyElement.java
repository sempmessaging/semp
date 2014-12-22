package net.davidtanzer.html;

public abstract class EmptyElement implements Node {
	private final TagName tagName;
	private Attributes attributes = new Attributes();

	protected EmptyElement(final TagName tagName) {
		this.tagName = tagName;
	}

	public void setAttribute(final AttributeName attributeName, final AttributeValue attributeValue) {
		attributes.add(attributeName, attributeValue);
	}

	@Override
	public void render(final StringBuilder renderedResultBuilder) {
		renderedResultBuilder.append("<");
		renderedResultBuilder.append(tagName.value());
		attributes.render(renderedResultBuilder);
		renderedResultBuilder.append(">");
	}
}
