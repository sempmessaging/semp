package org.sempmessaging.sempc.ui.viewmodel;

public interface PropertyChangedEvent {
	public <T> void propertyChanged(Property<T> property);
}
