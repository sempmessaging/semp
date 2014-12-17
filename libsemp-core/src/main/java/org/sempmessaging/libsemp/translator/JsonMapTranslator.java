package org.sempmessaging.libsemp.translator;

import net.davidtanzer.value.SingleValue;
import org.sempmessaging.libsemp.arguments.Args;

import java.lang.reflect.Constructor;
import java.util.Map;

public class JsonMapTranslator {
	private Map<String, Object> jsonMap;

	public <T> T read(final String jsonKey, final Class<T> valueClass, final RuntimeException exception) {
		checkJsonMap();
		if(jsonMap.containsKey(jsonKey)) {
			return readValue(jsonKey, valueClass);
		} else {
			throw exception;
		}
	}

	public <T> T read(final String jsonKey, final Class<T> valueClass, T defaultValue) {
		checkJsonMap();

		if(jsonMap.containsKey(jsonKey)) {
			return readValue(jsonKey, valueClass);
		} else {
			return defaultValue;
		}
	}

	public <T> T read(final String jsonKey, final Class<T> valueClass) {
		T defaultValue = getDefaultValueForObjectOrSingleValue(valueClass);
		return read(jsonKey, valueClass, (T) defaultValue);
	}

	private <T> T getDefaultValueForObjectOrSingleValue(final Class<T> valueClass) {
		//We have to use Object here, because otherwise we cannot convince the compiler that "SingleValue.empty(...)"
		//down below gets a correct method parameter: We could not cast the valueClass to anything that compiles.
		Object defaultValue = null;

		if(SingleValue.class.isAssignableFrom(valueClass)) {
			defaultValue = SingleValue.empty((Class<SingleValue>) valueClass);
		}

		return (T) defaultValue;
	}

	private void checkJsonMap() {
		if(jsonMap == null) {
			throw new IllegalStateException("JSON map must be set via \"readFrom(...)\" before calling a \"read(...)\" method.");
		}
	}

	private <T> T readValue(final String jsonKey, final Class<T> valueClass) {
		Object value = jsonMap.get(jsonKey);
		Constructor<?>[] constructors = valueClass.getConstructors();
		for(Constructor constructor : constructors) {
			if(constructor.getParameterTypes().length == 1 && constructor.getParameterTypes()[0].isAssignableFrom(value.getClass())) {
				try {
					return (T) constructor.newInstance(value);
				} catch (Exception e) {
					throw new IllegalStateException("Could not create net.davidtanzer.value object", e);
				}
			}
		}

		throw new IllegalStateException("Could not find suitable constructor for net.davidtanzer.value object "+valueClass.getSimpleName()+"("+value+").");
	}

	public void readFrom(final Map<String, Object> jsonMap) {
		Args.notNull(jsonMap, "jsonMap");
		Args.setOnce(this.jsonMap, "jsonMap");

		this.jsonMap = jsonMap;
	}
}
