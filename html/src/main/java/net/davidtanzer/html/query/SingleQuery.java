package net.davidtanzer.html.query;

import net.davidtanzer.html.Node;
import net.davidtanzer.html.query.criteria.Criteria;
import net.davidtanzer.html.values.AttributeName;

import java.util.List;

public class SingleQuery {
	private final Query query;

	SingleQuery(final Criteria... criteria) {
		this(null, criteria);
	}

	public SingleQuery(final Query baseQuery, final Criteria... criteria) {
		query = new Query(baseQuery, criteria);
	}

	public Node from(final Node... nodes) {
		List<Node> selectedNodes = query.from(nodes);
		return selectedNodes.get(0);
	}

	public AttributeQuery selectAttribute(final AttributeName attributeName) {
		return new AttributeQuery(this, attributeName);
	}
}
