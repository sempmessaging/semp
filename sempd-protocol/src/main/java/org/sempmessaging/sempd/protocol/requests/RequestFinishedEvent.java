package org.sempmessaging.sempd.protocol.requests;

import org.sempmessaging.sempd.protocol.requests.handler.RequestHandlerRunner;

public interface RequestFinishedEvent {
	void requestFinished(RequestHandlerRunner requestHandlerRunner);
}
