package org.sempmessaging.libsemp.request;

import java.util.Map;

public interface JSONSender {
	void send(Map<String, Object> object);
}
