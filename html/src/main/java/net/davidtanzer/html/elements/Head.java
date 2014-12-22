package net.davidtanzer.html.elements;

import net.davidtanzer.html.Element;
import net.davidtanzer.html.EmptyElement;
import net.davidtanzer.html.elements.values.CssLink;
import net.davidtanzer.html.elements.values.ScriptLink;
import net.davidtanzer.html.values.AttributeName;
import net.davidtanzer.html.values.AttributeValue;
import net.davidtanzer.html.values.TagName;

public class Head extends Element {
	private Title title;

	Head() {
		super(TagName.of("head"));
	}

	public void title(final Title title) {
		if(title != null) {
			throw new IllegalStateException("There can be only one title element within a page!");
		}
		this.title = title;
		add(title);
	}

	public void addCssLink(final CssLink cssLink) {
		add(new EmptyElement(TagName.of("link")) {{
			setAttribute(AttributeName.of("rel"), new AttributeValue("stylesheet"));
			setAttribute(AttributeName.of("href"), cssLink);
		}});
	}

	public void addScriptLink(final ScriptLink scriptLink) {
		add(new Element(TagName.of("script")) {{
			setAttribute(AttributeName.of("type"), new AttributeValue("text/javascript"));
			setAttribute(AttributeName.of("src"), scriptLink);
		}});
	}
}
