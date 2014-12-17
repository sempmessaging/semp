package net.davidtanzer.jevents.guice;

import com.google.inject.AbstractModule;
import net.davidtanzer.jevents.EventComponent;

public abstract class EventComponentModule extends AbstractModule {
	protected <T extends EventComponent> com.google.inject.binder.ScopedBindingBuilder bindEventComponent(Class<T> componentClass) {
		return bind(componentClass).toProvider(ECProvider.forComponent(componentClass, getMembersInjector(componentClass)));
	}
}
