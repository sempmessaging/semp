package net.davidtanzer.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Element implements Node {
	private final TagName tagName;
	private final List<Node> children = new ArrayList<>();
	private final Map<AttributeName, AttributeValue> attributes = new HashMap<>();

	protected Element(final TagName tagName) {
		this.tagName = tagName;
	}

	public void add(final Node node) {
		children.add(node);
	}

	public void setAttribute(final AttributeName attributeName, final AttributeValue attributeValue) {
		attributes.put(attributeName, attributeValue);
	}

	@Override
	public final String render() {
		StringBuilder renderedResultBuilder = new StringBuilder();

		renderedResultBuilder.append("<");
		renderedResultBuilder.append(tagName.value());
		renderAttributes(renderedResultBuilder);
		renderedResultBuilder.append(">");

		renderedResultBuilder.append("</");
		renderedResultBuilder.append(tagName.value());
		renderedResultBuilder.append(">");

		return renderedResultBuilder.toString();
	}

	private void renderAttributes(final StringBuilder renderedResultBuilder) {
		for(AttributeName name : attributes.keySet()) {
			renderedResultBuilder.append(" ");
			renderedResultBuilder.append(name.value());
			renderedResultBuilder.append("=\"");
			renderedResultBuilder.append(attributes.get(name).value());
			renderedResultBuilder.append("\"");
		}
	}
}
