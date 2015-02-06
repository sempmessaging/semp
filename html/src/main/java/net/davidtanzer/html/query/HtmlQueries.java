package net.davidtanzer.html.query;

import net.davidtanzer.html.Node;
import net.davidtanzer.html.query.criteria.CssClassCriteria;
import net.davidtanzer.html.query.criteria.DirectChildCriteria;
import net.davidtanzer.html.query.criteria.IdCriteria;
import net.davidtanzer.html.query.criteria.NodeTypeCriteria;
import net.davidtanzer.html.values.AttributeName;
import net.davidtanzer.html.values.CssClass;
import net.davidtanzer.html.values.Id;

public class HtmlQueries {
	public static SingleQuery select(final Id id) {
		return new SingleQuery(new IdCriteria(id));
	}

	public static Query select(final CssClass cssClass) {
		return new Query(new CssClassCriteria(cssClass));
	}

	public static Query select(final Class<? extends Node> nodeType) {
		return new Query(new NodeTypeCriteria(nodeType));
	}

	public static Query selectChildren() {
		return new Query(new DirectChildCriteria());
	}

	public static AttributeQuery selectAttribute(final AttributeName attributeName) {
		return new AttributeQuery(attributeName);
	}

	public static Query selectAll() {
		return new Query();
	}
}
