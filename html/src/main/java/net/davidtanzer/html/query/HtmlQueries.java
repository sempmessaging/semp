package net.davidtanzer.html.query;

import net.davidtanzer.html.query.criteria.IdCriteria;
import net.davidtanzer.html.values.Id;

public class HtmlQueries {
	public static SingleQuery select(final Id id) {
		SingleQuery query = new SingleQuery(new IdCriteria(id));
		return query;
	}
}
