package org.sempmessaging.libsemp.arguments;

import net.davidtanzer.value.SingleValue;

public class Args {
	public static void notNull(Object value, String argumentName) {
		is(value != null, argumentName, "must not be null.");
	}

	public static void notEmpty(String value, String argumentName) {
		notNull(value, argumentName);
		is(value.length() > 0, argumentName, "must not be empty.");
	}

	public static void is(boolean condition, String argumentName, String conditionDescription) {
		if(!condition) {
			throw new IllegalArgumentException("\""+argumentName+"\" "+conditionDescription);
		}
	}

	public static void notEmpty(final SingleValue<?> value, final String argumentName) {
		notNull(value, argumentName);
		is(value != SingleValue.empty(value.getClass()), argumentName, "must not be the empty value.");
	}

	public static void setOnce(final Object classMember, final String classMemberName) {
		if(classMember != null) {
			throw new IllegalStateException("Cannot set \""+classMemberName+"\" again, it already has a value.");
		}
	}
}
