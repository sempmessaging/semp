package net.davidtanzer.html.query;

import net.davidtanzer.html.Node;
import net.davidtanzer.html.query.criteria.Criteria;

import java.util.ArrayList;
import java.util.List;

public class Query {
	private final Criteria[] criterias;

	Query(final Criteria... criterias) {
		this.criterias = criterias;
	}

	public List<Node> from(final Node[] nodes) {
		List<Node> result = new ArrayList<>();

		for(Node node : nodes) {
			node.visit((currentNode) -> {
				if(matchesCriteria(currentNode)) {
					result.add(currentNode);
				}
			});
		}
		return result;
	}

	private boolean matchesCriteria(final Node currentNode) {
		boolean matches = true;
		for(Criteria c : criterias) {
			matches = c.matches(currentNode);
		}
		return matches;
	}
}
