package org.sempmessaging.sempd.protocol.io;

import org.junit.Before;
import org.junit.Test;
import org.sempmessaging.libsemp.request.JSONSender;
import org.sempmessaging.libsemp.request.RequestId;

import java.io.IOException;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ClientConnectionResponseDataWriterTest {
	private ClientConnectionResponseWriter responseWriter;
	private JSONSender jsonSender;

	@Before
	public void setup() {
		jsonSender = mock(JSONSender.class);

		responseWriter = new ClientConnectionResponseWriter();
		responseWriter.setJsonSender(jsonSender);
	}

	@Test
	public void doesNotSendDataUntilNextRun() {
		ResponseData responseData = new ResponseData();
		responseData.setRequestId(new RequestId("theRequestId"));

		responseWriter.writeResponse(responseData);

		verifyNoMoreInteractions(jsonSender);
	}

	@Test
	public void doesNotSendAnythingWhenNothingWasWritten() {
		responseWriter.run();

		verifyNoMoreInteractions(jsonSender);
	}

	@Test
	public void sendsDataToJsonSenderOnNextRun() throws IOException {
		ResponseData responseData = new ResponseData();
		responseData.setRequestId(new RequestId("theRequestId"));

		responseWriter.writeResponse(responseData);
		responseWriter.run();

		verify(jsonSender).send(any(Map.class));
	}

	@Test
	public void sendsEachResponseDataExactlyOnce() throws IOException {
		ResponseData responseData = new ResponseData();
		responseData.setRequestId(new RequestId("theRequestId"));

		responseWriter.writeResponse(responseData);
		responseWriter.run();
		verify(jsonSender).send(any(Map.class));

		responseWriter.run();
		verifyNoMoreInteractions(jsonSender);
	}
}
