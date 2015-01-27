package net.davidtanzer.html.query;

import net.davidtanzer.html.BaseElement;
import net.davidtanzer.html.Node;
import net.davidtanzer.html.values.AttributeName;
import net.davidtanzer.html.values.AttributeValue;

public class AttributeQuery {
	private final SingleQuery baseQuery;
	private final AttributeName attributeName;

	public AttributeQuery(final AttributeName attributeName) {
		this(null, attributeName);
	}

	public AttributeQuery(final SingleQuery baseQuery, final AttributeName attributeName) {
		this.baseQuery = baseQuery;
		this.attributeName = attributeName;
	}

	public AttributeValue from(final Node node) {
		Node nodeToProcess = node;
		if(baseQuery != null) {
			nodeToProcess = baseQuery.from(node);
		}
		if(nodeToProcess instanceof BaseElement) {
			return ((BaseElement) nodeToProcess).processAttribute(attributeName, (value) -> value);
		}
		return null;
	}
}
