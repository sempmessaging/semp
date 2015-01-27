package net.davidtanzer.html.query;

import net.davidtanzer.html.elements.Div;
import org.junit.Test;

import static net.davidtanzer.html.query.HtmlQueries.selectAll;

public class SingleQueryTest {
	@Test(expected = TooManyResultsException.class)
	public void throwsExceptionWhenMoreThanOneNodeWasFound() {
		selectAll().singleElement().from(new Div(), new Div());
	}
}
