package org.sempmessaging.libsemp.connection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import net.davidtanzer.jevents.ComponentCodeGenerator;
import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.guice.EventComponentModule;
import net.davidtanzer.jevents.guice.EventComponentsProvider;
import org.junit.Before;
import org.junit.Test;
import org.sempmessaging.libsemp.request.Request;

import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;

public class ServerConnectionRequestsTest {
	private ServerConnectionRequests connectionRequests;

	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new EventComponentModule() {
			@Override
			protected void configure() {
				EventComponentsProvider.setEventComponents(new EventComponents(new JavassistComponentCodeGenerator()));
				bindEventComponent(TestRequest.class);
			}
		});
		connectionRequests = new ServerConnectionRequests(injector);
	}

	@Test
	public void newRequestCreatesANewObject() {
		Request request = connectionRequests.newRequest(TestRequest.class);

		assertNotNull(request);
	}

	@Test
	public void newRequestCreatesARequestWithAValidId() {
		Request request = connectionRequests.newRequest(TestRequest.class);
		assumeNotNull(request);

		assertNotNull(request.id());
		assertTrue(request.id().value().length() > 0);
	}

	@Test
	public void sendsNewJsonDataToCorrectRequest() {
		TestRequest request = connectionRequests.newRequest(TestRequest.class);
		assumeNotNull(request);
		assumeNotNull(request.id());
		assumeTrue(request.id().value().length() > 0);

		connectionRequests.processJsonData("{ \"RequestId\": \"" + request.id().value() + "\" }");

		assertNotNull(request.jsonResponse);
		assertEquals(request.id().value(), request.jsonResponse.get("RequestId"));
	}

	@Test
	public void removesRequestThatIsCompletedFromOpenRequest() {
		TestRequest request = connectionRequests.newRequest(TestRequest.class);
		assumeNotNull(request);
		request.complete();

		connectionRequests.processJsonData("{ \"RequestId\": \"" + request.id().value() + "\" }");

		assertNull(request.jsonResponse);
	}

	public static abstract class TestRequest extends Request {
		private Map<String, Object> jsonResponse;

		public void complete() {
			send(requestCompleteEvent()).requestComplete(this);
		}

		@Override
		public void processResponse(final Map<String, Object> jsonResponse) {
			this.jsonResponse = jsonResponse;
		}
	}
}
