package org.sempmessaging.sempc.ui;

import net.davidtanzer.html.BaseElement;
import net.davidtanzer.html.values.AttributeName;
import net.davidtanzer.html.values.AttributeValue;
import net.davidtanzer.html.values.EventHandlerScript;
import org.sempmessaging.sempc.ui.event.EventHandler;
import org.sempmessaging.sempc.ui.event.EventHandlerProvider;
import org.sempmessaging.sempc.ui.event.EventHandlerTemplate;

import java.util.HashMap;
import java.util.Map;

public class JsEventTest {
	private int nextId = 0;
	private Map<String, EventHandler> eventHandlers = new HashMap<>();

	private EventHandlerProvider eventHandlerProvider = new EventHandlerProvider() {
		@Override
		public EventHandlerScript provideEventHandler(final EventHandler eventHandler) {
			nextId++;
			eventHandlers.put(String.valueOf(nextId), eventHandler);
			return new EventHandlerScript(String.valueOf(nextId));
		}

		@Override
		public EventHandlerTemplate provideEventHandlerTemplate(final EventHandler eventHandler) {
			nextId++;
			eventHandlers.put(String.valueOf(nextId), eventHandler);
			return new EventHandlerTemplate(String.valueOf(nextId)+"%s") {
				@Override
				protected void renderParameterToString(final StringBuilder paramsStringBuilder, final String param) {
					paramsStringBuilder.append(param);
				}
			};
		}
	};

	protected EventHandlerProvider eventHandlerProvider() {
		return eventHandlerProvider;
	}

	protected void click(final BaseElement element) {
		element.processAttribute(AttributeName.of("onclick"), this::handleJsEvent);
	}

	private Object handleJsEvent(final AttributeValue value) {
		if(value != null) {
			String eventHandlerValue = value.value();
			String[] eventHandlerParts = eventHandlerValue.split(", ");
			String eventHandlerId = eventHandlerParts[0];

			String[] eventHandlerParameters = new String[eventHandlerParts.length - 1];
			System.arraycopy(eventHandlerParts, 1, eventHandlerParameters, 0, eventHandlerParameters.length);

			EventHandler eventHandler = eventHandlers.get(eventHandlerId);
			if (eventHandler != null) {
				eventHandler.handleEvent(eventHandlerParameters);
			}
		}
		return null;
	}
}
