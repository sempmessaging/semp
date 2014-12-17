package org.sempmessaging.libsemp.connection;

import org.sempmessaging.libsemp.request.Request;
import org.sempmessaging.libsemp.request.RequestId;

public interface ConnectionRequests {
	<T extends Request> T newRequest(final Class<T> requestClass);
	void processJsonData(String jsonData);
}
