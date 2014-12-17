package org.sempmessaging.libsemp.request;

import org.sempmessaging.libsemp.arguments.Args;
import net.davidtanzer.value.StringValue;

import java.util.UUID;

public class RequestId extends StringValue {
	public RequestId(final String value) {
		super(value);
		Args.notEmpty(value, "value");
	}

	private RequestId() {
		super();
	}

	public static RequestId newRandomRequestId() {
		return new RequestId(UUID.randomUUID().toString());
	}
}
