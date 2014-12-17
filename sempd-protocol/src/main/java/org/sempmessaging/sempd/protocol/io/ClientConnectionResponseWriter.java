package org.sempmessaging.sempd.protocol.io;

import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.request.JSONSender;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientConnectionResponseWriter implements Runnable {
	private final ConcurrentLinkedQueue<ResponseData> responseQueue = new ConcurrentLinkedQueue<>();
	private JSONSender jsonSender;

	public void writeResponse(final ResponseData responseData) {
		Args.notNull(responseData, "responseData");
		assert jsonSender != null : "JSON Sender must be set before calling writeResponse";

		responseQueue.offer(responseData);
	}

	@Override
	public void run() {
		ResponseData nextResponseData = responseQueue.poll();

		if(nextResponseData != null) {
			jsonSender.send(nextResponseData.asMap());
		}
	}

	public void setJsonSender(final JSONSender jsonSender) {
		Args.notNull(jsonSender, "jsonSender");
		this.jsonSender = jsonSender;
	}
}
