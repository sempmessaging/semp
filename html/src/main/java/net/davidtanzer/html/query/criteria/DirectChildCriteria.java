package net.davidtanzer.html.query.criteria;

import net.davidtanzer.html.Element;
import net.davidtanzer.html.Node;

public class DirectChildCriteria implements Criteria {
	@Override
	public boolean matches(final Node node, final Node rootNode) {
		if(rootNode instanceof Element) {
			return ((Element) rootNode).containsChild(node);
		}
		return false;
	}
}
