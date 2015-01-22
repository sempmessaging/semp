package net.davidtanzer.jevents.guice;

import net.davidtanzer.jevents.EventComponents;

public class EventComponentsProvider {
	private static EventComponents eventComponents;

	public static EventComponents eventComponents() {
		return eventComponents;
	}

	public static void setEventComponents(final EventComponents eventComponents) {
		EventComponentsProvider.eventComponents = eventComponents;
	}
}
