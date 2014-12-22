package net.davidtanzer.html;

import net.davidtanzer.value.StringValue;

public class TagName extends StringValue {
	private TagName(final String value) {
		super(value);
	}

	private TagName() {
		super();
	}

	public static TagName of(final String name) {
		return new TagName(name);
	}
}
