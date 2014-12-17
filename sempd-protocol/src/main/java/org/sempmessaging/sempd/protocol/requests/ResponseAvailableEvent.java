package org.sempmessaging.sempd.protocol.requests;

import org.sempmessaging.libsemp.request.RequestId;
import org.sempmessaging.sempd.protocol.io.ResponseData;

public interface ResponseAvailableEvent {
	void responseAvailable(final RequestId requestId, ResponseData responseData);
}
