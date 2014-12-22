package net.davidtanzer.html.values;

import net.davidtanzer.value.StringValue;

public class AttributeName extends StringValue {
	private AttributeName(final String value) {
		super(value);
	}

	private AttributeName() {
		super();
	}

	public static AttributeName of(final String name) {
		return new AttributeName(name);
	}
}
