package org.sempmessaging.libsemp.request;

public interface ErrorDuringRequestEvent {
	void error(RequestError requestError);
}
