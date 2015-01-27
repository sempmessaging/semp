package net.davidtanzer.html.query.criteria;

import net.davidtanzer.html.BaseElement;
import net.davidtanzer.html.Node;
import net.davidtanzer.html.values.AttributeName;
import net.davidtanzer.html.values.Id;

public class IdCriteria implements Criteria {
	private Id id;

	public IdCriteria(final Id id) {
		this.id = id;
	}

	@Override
	public boolean matches(final Node node, final Node rootNode) {
		if(node instanceof BaseElement) {
			return ((BaseElement) node).processAttribute(AttributeName.of("id"), (Id value) -> id.equals(value));
		}
		return false;
	}
}
