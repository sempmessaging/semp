package org.sempmessaging.sempd.protocol.requests;

import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.sempmessaging.libsemp.request.RequestId;
import org.sempmessaging.sempd.protocol.async.AsyncExecutor;
import org.sempmessaging.sempd.protocol.io.ResponseData;
import org.sempmessaging.sempd.protocol.requests.handler.RequestHandlerRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class RequestHandlersTest {
	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	private RequestHandlers requestHandlers;
	private RequestRouter requestRouter;

	@Before
	public void setup() {
		requestHandlers = EventComponents.createComponent(RequestHandlers.class);
		requestRouter = mock(RequestRouter.class);
		requestHandlers.setRequestRouter(requestRouter);
	}

	@Test
	public void subscribesToRequestRoutedEvent() {
		verify(requestRouter).subscribe(any(RequestRoutedEvent.class), any());
	}

	@Test
	public void routesRequestBasedOnJsonData() {
		requestHandlers.processJsonData("{ \"RequestId\": \"SomeId\", \"FooRequest\": 2}");

		ArgumentCaptor<Map> jsonMapCaptor = ArgumentCaptor.forClass(Map.class);
		verify(requestRouter).routeRequest(jsonMapCaptor.capture());
		assertTrue(jsonMapCaptor.getValue().containsKey("FooRequest"));
	}

	@Test
	public void sendsJsonDataToRoutedRequest() {
		RequestHandlerRunner handler = mock(RequestHandlerRunner.class);
		Map<String,Object> jsonData = new HashMap<String,Object>() {{
			put("RequestId", "id");
		}};

		requestHandlers.handleRoutedRequest(handler, jsonData);

		verify(handler).handleRequest(new RequestId("id"), jsonData);
	}

	@Test
	public void sendsSecondRequestDirectlyToTheSameHandler() {
		RequestId id = RequestId.newRandomRequestId();
		RequestHandlerRunner handler = mock(RequestHandlerRunner.class);
		Map<String,Object> jsonData = new HashMap<String,Object>() {{
			put("RequestId", id.value());
		}};

		requestHandlers.handleRoutedRequest(handler, jsonData);

		requestHandlers.processJsonData("{ \"RequestId\": \"" + id.value() + "\", \"FooRequest\": 2}");
		ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);
		verify(handler, times(2)).handleRequest(eq(id), mapCaptor.capture());

		assertThat(mapCaptor.getValue().get("RequestId"), is(id.value()));
		assertThat(mapCaptor.getValue().get("FooRequest"), is(2.0));
	}

	@Test
	public void doesNotUseRequestRouterOnSecondRequest() {
		RequestId id = RequestId.newRandomRequestId();
		RequestHandlerRunner handler = mock(RequestHandlerRunner.class);
		Map<String,Object> jsonData = new HashMap<String,Object>() {{
			put("RequestId", id.value());
		}};

		requestHandlers.handleRoutedRequest(handler, jsonData);

		requestHandlers.processJsonData("{ \"RequestId\": \"" + id.value() + "\", \"FooRequest\": 2}");

		verify(requestRouter, never()).routeRequest(any());
	}

	@Test
	public void sendsResponseAvailableEventWhenRequestIsHandled() {
		TestRequestHandlerRunner handler = EventComponents.createComponent(TestRequestHandlerRunner.class);
		handler.setAsyncExecutor(mock(AsyncExecutor.class));
		Map<String,Object> jsonData = new HashMap<String,Object>() {{
			put("RequestId", "id");
		}};

		requestHandlers.handleRoutedRequest(handler, jsonData);
		ResponseData expectedResponseData = mock(ResponseData.class);

		eventTestRule.subscribeMandatory(requestHandlers, requestHandlers.responseDataAvailableEvent(),
				(response) -> assertSame(expectedResponseData, response));

		handler.sendResponseAvailable(expectedResponseData);
	}

	@Test
	public void doesNotFindHandlerAnymoreWhenRequestIsFinished() {
		RequestId id = RequestId.newRandomRequestId();
		TestRequestHandlerRunner handler = EventComponents.createComponent(TestRequestHandlerRunner.class);
		handler.setAsyncExecutor(mock(AsyncExecutor.class));
		Map<String,Object> jsonData = new HashMap<String,Object>() {{
			put("RequestId", id.value());
		}};

		requestHandlers.handleRoutedRequest(handler, jsonData);
		requestHandlers.processJsonData("{ \"RequestId\": \"" + id.value() + "\" }");
		verify(requestRouter, never()).routeRequest(any());

		handler.sendRequestFinished();

		requestHandlers.processJsonData("{ \"RequestId\": \""+id.value()+"\" }");
		verify(requestRouter, times(1)).routeRequest(any());
	}

	@Test
	public void setsCorrectRequestIdBeforeHandingTheResponseDataToTheResponseWriter() {
		TestRequestHandlerRunner handler = EventComponents.createComponent(TestRequestHandlerRunner.class);
		handler.setAsyncExecutor(mock(AsyncExecutor.class));
		Map<String,Object> jsonData = new HashMap<String,Object>() {{
			put("RequestId", "id");
		}};

		requestHandlers.handleRoutedRequest(handler, jsonData);
		ResponseData expectedResponseData = mock(ResponseData.class);

		RequestId requestId = new RequestId("expectedRequestId");
		handler.sendResponseAvailable(requestId, expectedResponseData);

		verify(expectedResponseData).setRequestId(eq(requestId));
	}

	public static abstract class TestRequestHandlerRunner extends RequestHandlerRunner {
		public void sendResponseAvailable(final ResponseData responseData) {
			sendResponseAvailable(RequestId.newRandomRequestId(), responseData);
		}

		public void sendResponseAvailable(final RequestId requestId, final ResponseData responseData) {
			send(responseAvailableEvent()).responseAvailable(requestId, responseData);
		}

		public void sendRequestFinished() {
			send(requestFinishedEvent()).requestFinished(this);
		}
	}
}
