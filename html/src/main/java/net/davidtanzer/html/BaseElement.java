package net.davidtanzer.html;

import net.davidtanzer.html.values.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseElement {
	private final TagName tagName;
	private List<CssClass> cssClasses = new ArrayList<>();
	private Attributes attributes = new Attributes();

	protected BaseElement(final TagName tagName) {
		this.tagName = tagName;
	}

	protected void setAttribute(final AttributeName attributeName, final AttributeValue attributeValue) {
		attributes.add(attributeName, attributeValue);
	}

	public void id(final Id id) {
		setAttribute(AttributeName.of("id"), id);
	}

	public void cssClass(final CssClass cssClass) {
		cssClasses.add(cssClass);
		setAttribute(AttributeName.of("class"), toCssClassString(cssClasses));
	}

	public void cssClasses(final CssClass... cssClasses) {
		this.cssClasses.addAll(Arrays.asList(cssClasses));
		setAttribute(AttributeName.of("class"), toCssClassString(this.cssClasses));
	}

	private CssClass toCssClassString(final List<CssClass> cssClasses) {
		StringBuilder resultBuilder = new StringBuilder();

		for(CssClass c : cssClasses) {
			if(resultBuilder.length() > 0) {
				resultBuilder.append(" ");
			}
			resultBuilder.append(c.value());
		}

		return new CssClass(resultBuilder.toString());
	}

	public void style(final CssStyle style) {
		setAttribute(AttributeName.of("style"), style);
	}

	protected void renderOpenTag(final StringBuilder renderedResultBuilder) {
		renderedResultBuilder.append("<");
		renderedResultBuilder.append(tagName.value());
		attributes.render(renderedResultBuilder);
		renderedResultBuilder.append(">");
	}

	protected void renderCloseTag(final StringBuilder renderedResultBuilder) {
		renderedResultBuilder.append("</");
		renderedResultBuilder.append(tagName.value());
		renderedResultBuilder.append(">");
	}
}
