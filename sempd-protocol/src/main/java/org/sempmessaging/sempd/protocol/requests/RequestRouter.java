package org.sempmessaging.sempd.protocol.requests;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.sempd.protocol.requests.handler.RequestHandler;
import org.sempmessaging.sempd.protocol.requests.handler.RequestHandlerRunner;
import org.sempmessaging.sempd.protocol.requests.serverinfo.GetServerInfoHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class RequestRouter extends EventComponent {
	private static final HashMap<String, Class<? extends RequestHandler>> handlersForKeys = new HashMap<String, Class<? extends RequestHandler>>() {{
		put("GetServerInfo", GetServerInfoHandler.class);
	}};
	private Injector injector;

	@Event
	public abstract RequestRoutedEvent requestRoutedEvent();

	public void routeRequest(final Map<String, Object> jsonData) {
		Set<String> requestHandlerKeys = handlersForKeys.keySet();

		for(String key : requestHandlerKeys) {
			if(jsonData.containsKey(key)) {
				routeRequestTo(key, jsonData);
			}
		}
	}

	private void routeRequestTo(final String key, final Map<String, Object> jsonData) {
		assert key != null;
		assert jsonData != null;
		assert handlersForKeys.containsKey(key);

		Class<? extends RequestHandler> handlerClass = handlersForKeys.get(key);

		assert injector != null : "Injector must be set by test setup or dependency injection";
		RequestHandlerRunner handlerRunner = injector.getInstance(RequestHandlerRunner.class);
		RequestHandler handler = injector.getInstance(handlerClass);
		handlerRunner.setRequestHandler(handler);

		send(requestRoutedEvent()).requestRouted(handlerRunner, jsonData);
	}

	@Inject
	public void setInjector(final Injector injector) {
		this.injector = injector;
	}
}
