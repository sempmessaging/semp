package net.davidtanzer.html.query.criteria;

import net.davidtanzer.html.BaseElement;
import net.davidtanzer.html.Node;
import net.davidtanzer.html.values.AttributeName;
import net.davidtanzer.html.values.CssClass;
import net.davidtanzer.html.values.Id;

public class CssClassCriteria implements Criteria {
	private final CssClass cssClass;

	public CssClassCriteria(final CssClass cssClass)  {
		this.cssClass = cssClass;
	}

	@Override
	public boolean matches(final Node node, final Node rootNode) {
		if(node instanceof BaseElement) {
			return ((BaseElement) node).processAttribute(AttributeName.of("class"), (CssClass value) -> containsCssClass(value));
		}
		return false;
	}

	private boolean containsCssClass(final CssClass value) {
		if(value != null) {
			String[] classNames = value.value().split(" ");

			for (String name : classNames) {
				if (name.equals(cssClass.value())) {
					return true;
				}
			}
		}
		return false;
	}
}
