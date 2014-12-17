package org.sempmessaging.libsemp.request;

import org.sempmessaging.libsemp.arguments.Args;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestData {
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
}
