package net.davidtanzer.html.query;

import net.davidtanzer.html.query.criteria.DirectChildCriteria;
import net.davidtanzer.html.query.criteria.IdCriteria;
import net.davidtanzer.html.values.AttributeName;
import net.davidtanzer.html.values.Id;

public class HtmlQueries {
	public static SingleQuery select(final Id id) {
		return new SingleQuery(new IdCriteria(id));
	}

	public static Query selectChildren() {
		return new Query(new DirectChildCriteria());
	}

	public static AttributeQuery selectAttribute(final AttributeName attributeName) {
		return new AttributeQuery(attributeName);
	}
}
