package org.sempmessaging.sempd.protocol.requests.handler;

import org.sempmessaging.libsemp.arguments.Args;

public abstract class RequestStateValidatingRequestHandler implements RequestHandler {
	private Object currentState;

	public RequestStateValidatingRequestHandler(final Object initialState) {
		Args.notNull(initialState, "initialState");
		currentState = initialState;
	}

	protected abstract Object finishedState();
	protected abstract boolean isTransitionAllowed(final Object currentState, final Object newState);

	protected synchronized void transitionTo(final Object newState) {
		Args.notNull(newState, "newState");

		assert currentState != null : "State can never be set to a null net.davidtanzer.value.";
		if(currentState.equals(newState)) {
			throw new IllegalStateException("Transition to same state is never allowed. Current state: "+currentState);
		}
		if(!isTransitionAllowed(currentState, newState)) {
			throw new IllegalStateException("Transition " + currentState + " -> " + newState + " is not allowed.");
		}

		currentState = newState;
	}

	@Override
	public boolean isFinished() {
		assert currentState != null : "State can never be set to a null net.davidtanzer.value.";
		return currentState.equals(finishedState());
	}
}
