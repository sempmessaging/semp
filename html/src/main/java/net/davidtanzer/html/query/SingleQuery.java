package net.davidtanzer.html.query;

import net.davidtanzer.html.Node;
import net.davidtanzer.html.query.criteria.Criteria;

import java.util.List;

public class SingleQuery {
	private final Query query;

	SingleQuery(final Criteria... criterias) {
		query = new Query(criterias);
	}

	public Node from(final Node... nodes) {
		List<Node> selectedNodes = query.from(nodes);
		return selectedNodes.get(0);
	}
}
