package org.sempmessaging.libsemp;

import org.hamcrest.CustomMatcher;

import java.util.function.Predicate;

import static org.mockito.Matchers.argThat;

public class PredicateMatcher {
	public static <T> T argMatches(final Predicate<T> predicate, final String description) {
		return argThat(new CustomMatcher<T>(description) {
			@Override
			public boolean matches(final Object item) {
				return predicate.test((T) item);
			}
		});
	}
}
