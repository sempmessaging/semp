package org.sempmessaging.sempc.ui;

import net.davidtanzer.html.BaseElement;
import net.davidtanzer.html.values.AttributeName;
import net.davidtanzer.html.values.EventHandlerScript;
import org.sempmessaging.sempc.ui.event.EventHandler;
import org.sempmessaging.sempc.ui.event.EventHandlerProvider;

import java.util.HashMap;
import java.util.Map;

public class JsEventTest {
	private int nextId = 0;
	private Map<String, EventHandler> eventHandlers = new HashMap<>();

	private EventHandlerProvider eventHandlerProvider = eventHandler -> {
		nextId++;
		eventHandlers.put(String.valueOf(nextId), eventHandler);
		return new EventHandlerScript(String.valueOf(nextId));
	};

	protected EventHandlerProvider eventHandlerProvider() {
		return eventHandlerProvider;
	}

	protected void click(final BaseElement element) {
		element.processAttribute(AttributeName.of("onclick"), (value) -> {
			if(value != null) {
				EventHandler eventHandler = eventHandlers.get(value.value());
				if (eventHandler != null) {
					eventHandler.handleEvent();
				}
			}
			return null;
		});
	}
}
