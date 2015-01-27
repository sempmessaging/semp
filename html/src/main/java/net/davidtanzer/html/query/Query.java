package net.davidtanzer.html.query;

import net.davidtanzer.html.Node;
import net.davidtanzer.html.query.criteria.Criteria;
import net.davidtanzer.html.values.AttributeName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Query {
	private final Query baseQuery;
	private final Criteria[] criteria;

	Query(final Criteria... criteria) {
		this(null, criteria);
	}

	public Query(final Query baseQuery, final Criteria... criteria) {
		this.baseQuery = baseQuery;
		this.criteria = criteria;
	}

	public List<Node> from(final Node... nodes) {
		List<Node> nodesToProcess = Arrays.asList(nodes);
		if(baseQuery != null) {
			nodesToProcess = baseQuery.from(nodes);
		}

		List<Node> result = new ArrayList<>();

		for(Node node : nodesToProcess) {
			visitRootNode(result, node);
		}
		return result;
	}

	private void visitRootNode(final List<Node> result, final Node rootNode) {
		rootNode.visit((currentNode) -> {
			if (matchesCriteria(currentNode, rootNode)) {
				result.add(currentNode);
			}
		});
	}

	private boolean matchesCriteria(final Node currentNode, final Node rootNode) {
		boolean matches = true;
		for(Criteria c : criteria) {
			matches = c.matches(currentNode, rootNode);
		}
		return matches;
	}

	public AttributeQuery selectAttribute(final AttributeName attributeName) {
		return singleElement().selectAttribute(attributeName);
	}

	private SingleQuery singleElement() {
		return new SingleQuery(this);
	}
}
