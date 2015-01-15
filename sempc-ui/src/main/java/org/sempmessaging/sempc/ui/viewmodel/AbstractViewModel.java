package org.sempmessaging.sempc.ui.viewmodel;

import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.sempc.core.account.AccountStatus;

import java.util.List;

public abstract class AbstractViewModel extends EventComponent {
	@Event
	public abstract PropertyChangedEvent propertyChangedEvent();

	protected <T> void propertyChanged(final Property<T> property) {
		send(propertyChangedEvent()).propertyChanged(property);
	}

	protected <T> Property<T> newProperty(final Class<T> valueClass) {
		return new Property<>(this);
	}

	protected <T> Property<List<T>> newListProperty(final Class<T> valueClass) {
		return new Property<>(this);
	}
}
