package org.sempmessaging.sempd.protocol.io;

import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.request.RequestId;

import java.util.*;

public class ResponseData {
	private Map<String, Object> map = new HashMap<>();

	public void setRequestId(final RequestId requestId) {
		Args.notEmpty(requestId, "requestId");
		map.put("RequestId", requestId.value());
	}

	public Map<String, Object> asMap() {
		return Collections.unmodifiableMap(map);
	}

	protected void set(final String name, final Object value) {
		map.put(name, value);
	}

	protected void set(final String name, final List<?> values) {
		ArrayList<Object> convertedValues = new ArrayList<Object>();

		for(Object value : values) {
			convertedValues.add(convert(value));
		}

		map.put(name, convertedValues);
	}

	private Object convert(final Object value) {
		if(value instanceof ResponseData) {
			return ((ResponseData) value).asMap();
		}
		return value;
	}
}
