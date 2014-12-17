package org.sempmessaging.libsemp.request;

public class RequestError extends Exception {
	private final String description;

	public RequestError(final String description) {
		super(description);
		this.description = description;
	}

	public String description() {
		return description;
	}
}
