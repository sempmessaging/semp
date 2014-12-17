package net.davidtanzer.value;

public class StringValue extends SingleValue<String> {
	public StringValue(final String value) {
		super(value);
	}

	protected StringValue() {
		super(EmptyValue.EMPTY);
	}
}
