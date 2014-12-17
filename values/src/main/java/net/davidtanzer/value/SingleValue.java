package net.davidtanzer.value;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class SingleValue<BaseType> {
	public static enum EmptyValue {
		EMPTY
	}

	private static Map<Class<? extends SingleValue>, SingleValue> emptyValues = new HashMap<>();
	private final BaseType value;

	protected SingleValue(BaseType value) {
		if(value == null) {
			throw new IllegalArgumentException("Value must not be null");
		}
		this.value = value;
	}

	protected SingleValue(EmptyValue emptyValue) {
		this.value = null;
	}

	public BaseType value() {
		return value;
	}

	public static <T extends SingleValue> T empty(Class<T> valueClass) {
		if(!emptyValues.containsKey(valueClass)) {
			T emptyValue = null;
			try {
				Constructor<T> constructor = valueClass.getDeclaredConstructor();
				constructor.setAccessible(true);
				emptyValue = constructor.newInstance();
			} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
				throw new IllegalStateException("Could not create empty net.davidtanzer.value", e);
			}
			emptyValues.put(valueClass, emptyValue);
		}
		return (T) emptyValues.get(valueClass);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SingleValue value1 = (SingleValue) o;

		if (value != null ? !value.equals(value1.value) : value1.value != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return value != null ? value.hashCode() : 0;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+"("+String.valueOf(value)+")";
	}
}
