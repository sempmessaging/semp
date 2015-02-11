package net.davidtanzer.value;

public class IntegerValue extends SingleValue<Integer> {
	public IntegerValue(final int value) {
		super(Integer.valueOf(value));
	}

	protected IntegerValue() {
		super(EmptyValue.EMPTY);
	}
}
