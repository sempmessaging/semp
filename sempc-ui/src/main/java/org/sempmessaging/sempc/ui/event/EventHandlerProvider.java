package org.sempmessaging.sempc.ui.event;

import net.davidtanzer.html.values.EventHandlerScript;

public interface EventHandlerProvider {
	EventHandlerScript provideEventHandler(EventHandler eventHandler);
	EventHandlerTemplate provideEventHandlerTemplate(EventHandler eventHandler);
}
