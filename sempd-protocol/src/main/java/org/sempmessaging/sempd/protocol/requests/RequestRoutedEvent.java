package org.sempmessaging.sempd.protocol.requests;

import org.sempmessaging.sempd.protocol.requests.handler.RequestHandlerRunner;

import java.util.Map;

public interface RequestRoutedEvent {
	void requestRouted(final RequestHandlerRunner handler, final Map<String, Object> jsonData);
}
