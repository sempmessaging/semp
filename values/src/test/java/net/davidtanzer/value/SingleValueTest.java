package net.davidtanzer.value;

import org.junit.Assume;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class SingleValueTest {
	public static class TestValue extends SingleValue<String> {
		private TestValue() {
			super(EmptyValue.EMPTY);
		}
	}

	@Test
	public void emptyReturnsAValidObject() {
		SingleValue emptyValue = SingleValue.empty(TestValue.class);

		assertNotNull(emptyValue);
	}

	@Test
	public void emptyValueReturnsTheSameObjectOnEveryCall() {
		SingleValue emptyValue1 = SingleValue.empty(TestValue.class);
		SingleValue emptyValue2 = SingleValue.empty(TestValue.class);
		Assume.assumeNotNull(emptyValue1);
		Assume.assumeNotNull(emptyValue2);

		assertSame(emptyValue1, emptyValue2);
	}
}
