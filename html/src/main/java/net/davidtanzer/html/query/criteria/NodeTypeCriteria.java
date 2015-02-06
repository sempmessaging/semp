package net.davidtanzer.html.query.criteria;

import net.davidtanzer.html.Node;

public class NodeTypeCriteria implements Criteria {
	private Class<? extends Node> nodeType;

	public NodeTypeCriteria(final Class<? extends Node> nodeType) {
		this.nodeType = nodeType;
	}

	@Override
	public boolean matches(final Node node, final Node rootNode) {
		return node.getClass().equals(nodeType);
	}
}
