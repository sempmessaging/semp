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
		return new EventHandlerTemplate("window.eventHandlerProvider.onEvent(\'"+lastId+"%s\');");
	}

	public void onEvent(final String eventId) {
		registeredEventHandlers.get(eventId).handleEvent();
	}
}
