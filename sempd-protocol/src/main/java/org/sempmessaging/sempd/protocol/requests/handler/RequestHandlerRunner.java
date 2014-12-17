package org.sempmessaging.sempd.protocol.requests.handler;

import com.google.inject.Inject;
import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.request.RequestId;
import org.sempmessaging.sempd.protocol.async.AsyncExecutor;
import org.sempmessaging.sempd.protocol.io.ResponseData;
import org.sempmessaging.sempd.protocol.requests.RequestFinishedEvent;
import org.sempmessaging.sempd.protocol.requests.ResponseAvailableEvent;

import java.util.Map;

public abstract class RequestHandlerRunner extends EventComponent {
	private AsyncExecutor asyncExecutor;
	private RequestHandler requestHandler;

	@Event
	public abstract ResponseAvailableEvent responseAvailableEvent();

	@Event
	public abstract RequestFinishedEvent requestFinishedEvent();

	public void handleRequest(final RequestId requestId, final Map<String, Object> jsonData) {
		Args.notNull(jsonData, "jsonData");

		asyncExecutor.execute(() -> {
			ResponseData responseData = requestHandler.getResponseFor(jsonData);
			send(responseAvailableEvent()).responseAvailable(requestId, responseData);

			if(requestHandler.isFinished()) {
				send(requestFinishedEvent()).requestFinished(this);
			}

			//FIXME Handle errors!
		});
	}

	public void setRequestHandler(final RequestHandler requestHandler) {
		this.requestHandler = requestHandler;
	}

	@Inject
	public void setAsyncExecutor(final AsyncExecutor asyncExecutor) {
		this.asyncExecutor = asyncExecutor;
	}
}
