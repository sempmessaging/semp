package net.davidtanzer.html;

import java.util.ArrayList;
import java.util.List;

public abstract class Element implements Node {
	private final TagName tagName;
	private final List<Node> children = new ArrayList<>();
	private Attributes attributes = new Attributes();

	protected Element(final TagName tagName) {
		this.tagName = tagName;
	}

	protected void add(final Node node) {
		children.add(node);
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

		renderChildren(renderedResultBuilder);

		renderedResultBuilder.append("</");
		renderedResultBuilder.append(tagName.value());
		renderedResultBuilder.append(">");
	}

	private void renderChildren(final StringBuilder renderedResultBuilder) {
		for(Node child : children) {
			child.render(renderedResultBuilder);
		}
	}

}
