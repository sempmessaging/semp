package org.sempmessaging.sempd.protocol.requests.handler;

import org.sempmessaging.sempd.protocol.io.ResponseData;

import java.util.Map;

public interface RequestHandler {
	ResponseData getResponseFor(Map<String, Object> jsonData);
	boolean isFinished();
}
