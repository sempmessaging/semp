package org.sempmessaging.libsemp.request;

import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.libsemp.arguments.Args;

import java.util.Map;

public abstract class Request extends EventComponent {
	private RequestId id;
	private JSONSender jsonSender;

	@Event
	public abstract RequestCompleteEvent requestCompleteEvent();
	@Event
	public abstract ErrorDuringRequestEvent errorDuringRequest();

	public RequestId id() {
		return id;
	}

	public void sendJsonTo(final JSONSender jsonSender) {
		this.jsonSender = jsonSender;
	}

	public abstract void processResponse(final Map<String, Object> jsonResponse);

	public void start() {
		RequestData requestData = createRequestData();
		fillRequestData(requestData);

		jsonSender.send(requestData.asMap());
	}

	private void fillRequestData(final RequestData requestData) {
		requestData.setRequestId(id());
	}

	protected abstract RequestData createRequestData();

	public void setId(final RequestId id) {
		Args.notEmpty(id.value(), "id");

		if(this.id != null) {
			throw new IllegalStateException("Can not set id again (id was already set to \""+id+"\").");
		}

		this.id = id;
	}
}
