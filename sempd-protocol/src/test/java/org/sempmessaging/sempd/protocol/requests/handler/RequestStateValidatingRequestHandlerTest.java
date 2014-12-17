package org.sempmessaging.sempd.protocol.requests.handler;

import org.junit.Test;
import org.sempmessaging.sempd.protocol.io.ResponseData;

import java.util.Map;

import static org.junit.Assert.assertTrue;

public class RequestStateValidatingRequestHandlerTest {
	@Test
	public void transitionToFinishedStateCausesIsFinishedToBecomeTrue() {
		TestHandler handler = new TestHandler() {
			@Override
			public ResponseData getResponseFor(final Map<String, Object> jsonData) {
				transitionTo(TestHandlerState.FINISHED);
				return null;
			}
		};

		handler.getResponseFor(null);

		assertTrue(handler.isFinished());
	}

	@Test(expected = IllegalStateException.class)
	public void transitionToSameStateThrowsException() {
		TestHandler handler = new TestHandler() {
			@Override
			public ResponseData getResponseFor(final Map<String, Object> jsonData) {
				transitionTo(TestHandlerState.INITIAL);
				return null;
			}
		};

		handler.getResponseFor(null);
	}

	@Test(expected = IllegalStateException.class)
	public void transitionToIllegalStateThrowsException() {
		TestHandler handler = new TestHandler() {
			@Override
			public ResponseData getResponseFor(final Map<String, Object> jsonData) {
				transitionTo(TestHandlerState.WAITING_FOR_MORE_DATA);
				return null;
			}

			@Override
			protected boolean isTransitionAllowed(final Object currentState, final Object newState) {
				return false;
			}
		};

		handler.getResponseFor(null);
	}

	public static abstract class TestHandler extends RequestStateValidatingRequestHandler {
		public static enum TestHandlerState {
			INITIAL, WAITING_FOR_MORE_DATA, FINISHED
		}

		public TestHandler() {
			super(TestHandlerState.INITIAL);
		}

		@Override
		protected Object finishedState() {
			return TestHandlerState.FINISHED;
		}

		@Override
		protected boolean isTransitionAllowed(final Object currentState, final Object newState) {
			return true;
		}
	}
}
