package org.sempmessaging.libsemp.connection;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.request.Request;
import org.sempmessaging.libsemp.request.RequestId;

import java.util.HashMap;
import java.util.Map;

public class ServerConnectionRequests implements ConnectionRequests {
	private Map<RequestId, Request> openRequests = new HashMap<>();
	private Injector injector;

	@Inject
	public ServerConnectionRequests(final Injector injector) {
		Args.notNull(injector, "injector");
		this.injector = injector;
	}

	@Override
	public <T extends Request> T newRequest(final Class<T> requestClass) {
		Args.notNull(requestClass, "requestClass");

		T request = createNewRequest(requestClass);
		registerNewRequest(request);
		return request;
	}

	@Override
	public void processJsonData(final String jsonData) {
		Args.notEmpty(jsonData, "jsonData");

		Map<String, Object> jsonMap = new Gson().fromJson(jsonData, new TypeToken<Map<String, Object>>() {
		}.getType());
		//FIXME check json map for validity (contains id, ...)

		dispatchResponseToHandler(jsonMap);
	}

	private void dispatchResponseToHandler(final Map<String, Object> jsonMap) {
		RequestId requestId = getRequestIdFrom(jsonMap);

		Request request = openRequests.get(requestId);
		if(request != null) {
			request.processResponse(jsonMap);
		} else {
			//FIXME what should we do here? Simply ignore?
		}
	}

	private RequestId getRequestIdFrom(final Map<String, Object> jsonMap) {
		String requestId = (String) jsonMap.get("RequestId");
		if(requestId == null) {
			throw new UnsupportedOperationException("FIXME: Request invalid. What should we do in this case?");
		}
		return new RequestId(requestId);
	}

	private <T extends Request> void registerNewRequest(final T request) {
		assert request != null : "The caller must make sure that request != null";

		request.subscribe(request.requestCompleteEvent(), this::removeRequest);

		openRequests.put(request.id(), request);
	}

	private void removeRequest(Request request) {
		assert request != null : "The caller must make sure that request != null";
		assert openRequests.containsKey(request.id()) : "Only open requests can be removed. No request must send the complete event twice.";
		openRequests.remove(request.id());
	}

	private <T extends Request> T createNewRequest(final Class<T> requestClass) {
		assert requestClass != null : "The caller must make sure that requestClass != null";

		T request = injector.getInstance(requestClass);
		RequestId newRequestId = RequestId.newRandomRequestId();
		request.setId(newRequestId);
		return request;
	}

}
