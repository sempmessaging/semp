package net.davidtanzer.value;

import java.time.ZonedDateTime;

public class DateValue extends SingleValue<ZonedDateTime> {
	public DateValue(final ZonedDateTime value) {
		super(value);
	}

	protected DateValue() {
		super(EmptyValue.EMPTY);
	}
}
