package net.davidtanzer.html;

import java.util.HashMap;
import java.util.Map;

public class Attributes {
	private final Map<AttributeName, AttributeValue> attributes = new HashMap<>();

	public void add(final AttributeName attributeName, final AttributeValue attributeValue) {
		attributes.put(attributeName, attributeValue);
	}

	public void render(final StringBuilder renderedResultBuilder) {
		for(AttributeName name : attributes.keySet()) {
			renderedResultBuilder.append(" ");
			renderedResultBuilder.append(name.value());
			renderedResultBuilder.append("=\"");
			renderedResultBuilder.append(attributes.get(name).value());
			renderedResultBuilder.append("\"");
		}
	}
}
