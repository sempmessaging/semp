package org.sempmessaging.sempd.protocol.requests.handler;

import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.sempmessaging.libsemp.request.RequestId;
import org.sempmessaging.sempd.protocol.async.AsyncExecutor;
import org.sempmessaging.sempd.protocol.io.ResponseData;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RequestHandlerRunnerTest {
	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	private AsyncExecutor asyncExecutor;
	private RequestHandlerRunner requestHandlerRunner;

	@Before
	public void setup() {
		asyncExecutor = mock(AsyncExecutor.class);
		requestHandlerRunner = EventComponents.createComponent(RequestHandlerRunner.class);
		requestHandlerRunner.setAsyncExecutor(asyncExecutor);
		requestHandlerRunner.setRequestHandler(new TestRequestHandler());
	}

	@Test
	public void usesAsyncExecutorToScheduleATaskWhenHandlingARequest() {
		requestHandlerRunner.handleRequest(RequestId.newRandomRequestId(), new HashMap<>());

		verify(asyncExecutor).execute(any(Runnable.class));
	}

	@Test
	public void sendsResponseAvailableAfterProcessingRequest() {
		eventTestRule.subscribeMandatory(requestHandlerRunner, requestHandlerRunner.responseAvailableEvent(), (id, r) -> assertNotNull(r));

		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		requestHandlerRunner.handleRequest(RequestId.newRandomRequestId(), new HashMap<>());
		verify(asyncExecutor).execute(captor.capture());

		captor.getValue().run();
	}

	@Test
	public void sendsRequestFinishedEventAfterProcessingRequest() {
		eventTestRule.subscribeMandatory(requestHandlerRunner, requestHandlerRunner.requestFinishedEvent(), (r) -> assertEquals(requestHandlerRunner, r));

		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		requestHandlerRunner.handleRequest(RequestId.newRandomRequestId(), new HashMap<>());
		verify(asyncExecutor).execute(captor.capture());

		captor.getValue().run();
	}

	public static class TestRequestHandler implements RequestHandler {
		@Override
		public ResponseData getResponseFor(final Map<String, Object> jsonData) {
			return mock(ResponseData.class);
		}

		@Override
		public boolean isFinished() {
			return true;
		}
	}
}
