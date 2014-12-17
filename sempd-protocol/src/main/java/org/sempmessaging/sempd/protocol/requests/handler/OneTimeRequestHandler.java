package org.sempmessaging.sempd.protocol.requests.handler;

import org.sempmessaging.sempd.protocol.io.ResponseData;

import java.util.Map;

public abstract class OneTimeRequestHandler extends RequestStateValidatingRequestHandler {
	public static enum RequestState {
		INITIAL, FINISHED
	}

	public OneTimeRequestHandler() {
		super(RequestState.INITIAL);
	}

	@Override
	protected Object finishedState() {
		return RequestState.FINISHED;
	}

	@Override
	protected boolean isTransitionAllowed(final Object currentState, final Object newState) {
		return true;
	}

	@Override
	public ResponseData getResponseFor(final Map<String, Object> jsonData) {
		transitionTo(RequestState.FINISHED);
		return safelyGetResponseFor(jsonData);
	}

	protected abstract ResponseData safelyGetResponseFor(final Map<String, Object> jsonData);
}
