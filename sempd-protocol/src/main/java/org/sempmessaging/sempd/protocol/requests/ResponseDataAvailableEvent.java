package org.sempmessaging.sempd.protocol.requests;

import org.sempmessaging.sempd.protocol.io.ResponseData;

public interface ResponseDataAvailableEvent {
	void responseDataAvailable(ResponseData response);
}
