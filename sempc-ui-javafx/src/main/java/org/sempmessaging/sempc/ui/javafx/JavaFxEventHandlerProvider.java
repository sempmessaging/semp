package org.sempmessaging.sempc.ui.javafx;

import com.google.inject.Singleton;
import net.davidtanzer.html.values.EventHandlerScript;
import org.sempmessaging.sempc.ui.event.EventHandler;
import org.sempmessaging.sempc.ui.event.EventHandlerProvider;
import org.sempmessaging.sempc.ui.event.EventHandlerTemplate;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class JavaFxEventHandlerProvider implements EventHandlerProvider {
	private static long lastId = 0;
	private Map<String, EventHandler> registeredEventHandlers = new HashMap<>();

	@Override
	public EventHandlerScript provideEventHandler(final EventHandler eventHandler) {
		lastId++;
		registeredEventHandlers.put(String.valueOf(lastId), eventHandler);
		return new EventHandlerScript("eventHandlerProvider.onEvent(\'"+lastId+"\')");
	}

	@Override
	public EventHandlerTemplate provideEventHandlerTemplate(final EventHandler eventHandler) {
		lastId++;
		registeredEventHandlers.put(String.valueOf(lastId), eventHandler);
		return new EventHandlerTemplate("window.eventHandlerProvider.onEvent%s(\'"+lastId+"\'%s);");
	}

	public void onEvent(final String eventId) {
		registeredEventHandlers.get(eventId).handleEvent();
	}

	public void onEvent1(final String eventId, String param1) {
		registeredEventHandlers.get(eventId).handleEvent(param1);
	}

	public void onEvent2(final String eventId, String param1, String param2) {
		registeredEventHandlers.get(eventId).handleEvent(param1, param2);
	}

	public void onEvent3(final String eventId, String param1, String param2, String param3) {
		registeredEventHandlers.get(eventId).handleEvent(param1, param2, param3);
	}

	public void onEvent4(final String eventId, String param1, String param2, String param3, String param4) {
		registeredEventHandlers.get(eventId).handleEvent(param1, param2, param3, param4);
	}

	public void onEvent5(final String eventId, String param1, String param2, String param3, String param4, String param5) {
		registeredEventHandlers.get(eventId).handleEvent(param1, param2, param3, param4, param5);
	}
}
