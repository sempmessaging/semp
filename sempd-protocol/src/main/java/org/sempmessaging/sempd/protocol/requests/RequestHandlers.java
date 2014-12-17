package org.sempmessaging.sempd.protocol.requests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.request.RequestId;
import org.sempmessaging.sempd.protocol.io.ResponseData;
import org.sempmessaging.sempd.protocol.requests.handler.RequestHandlerRunner;

import java.util.HashMap;
import java.util.Map;

public abstract class RequestHandlers extends EventComponent {
	private RequestRouter requestRouter;
	private Map<RequestId, RequestHandlerRunner> handlers = new HashMap<>();
	private Map<RequestHandlerRunner, RequestId> idsOfHandlers = new HashMap<>();

	@Event
	public abstract ResponseDataAvailableEvent responseDataAvailableEvent();

	public void processJsonData(final String jsonData) {
		Args.notEmpty(jsonData, "jsonData");

		Map<String, Object> jsonMap = new Gson().fromJson(jsonData, new TypeToken<Map<String, Object>>() {}.getType());
		//FIXME check json map for validity (contains id, ...)

		dispatchRequestToHandler(jsonMap);
	}

	private void dispatchRequestToHandler(final Map<String, Object> jsonMap) {
		RequestId requestId = getRequestIdFrom(jsonMap);
		if(handlers.containsKey(requestId)) {
			handlers.get(requestId).handleRequest(requestId, jsonMap);
		} else {
			requestRouter.routeRequest(jsonMap);
		}
	}

	private RequestId getRequestIdFrom(final Map<String, Object> jsonMap) {
		String requestId = (String) jsonMap.get("RequestId");
		if(requestId == null) {
			throw new UnsupportedOperationException("FIXME: Request invalid. What should we do in this case?");
		}
		return new RequestId(requestId);
	}

	void handleRoutedRequest(final RequestHandlerRunner handler, final Map<String, Object> jsonData) {
		Args.notNull(handler, "handler");
		Args.notNull(jsonData, "jsonData");

		assert jsonData.containsKey("RequestId") : "Only requests with a correct id are routed.";
		RequestId requestId = getRequestIdFrom(jsonData);
		if(handlers.containsKey(requestId)) {
			throw new IllegalStateException("FIXME: This is clearly illegal. What should we do in this case?");
		}

		storeHandlerForSubsequentRequests(requestId, handler);
		registerWithHandlerEvents(handler);

		handler.handleRequest(requestId, jsonData);
	}

	private void registerWithHandlerEvents(final RequestHandlerRunner handler) {
		handler.subscribe(handler.responseAvailableEvent(), this::sendResponseWithCorrectRequestId);
		handler.subscribe(handler.requestFinishedEvent(), this::finishRequest);
	}

	private void sendResponseWithCorrectRequestId(final RequestId requestId, final ResponseData responseData) {
		responseData.setRequestId(requestId);
		send(responseDataAvailableEvent()).responseDataAvailable(responseData);
	}

	private void storeHandlerForSubsequentRequests(final RequestId requestId, final RequestHandlerRunner handler) {
		handlers.put(requestId, handler);
		idsOfHandlers.put(handler, requestId);
	}

	private void finishRequest(final RequestHandlerRunner handler) {
		assert handler != null;

		RequestId id = idsOfHandlers.get(handler);
		if(id == null) {
			throw new IllegalStateException("FIXME: This is clearly illegal. What should we do in this case?");
		}

		idsOfHandlers.remove(handler);
		handlers.remove(id);
	}

	@Inject
	public void setRequestRouter(final RequestRouter requestRouter) {
		this.requestRouter = requestRouter;
		requestRouter.subscribe(requestRouter.requestRoutedEvent(), (handler, jsonData) -> handleRoutedRequest(handler, jsonData));
	}
}
