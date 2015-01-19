package net.davidtanzer.jevents.guice;

import com.google.inject.MembersInjector;
import com.google.inject.Provider;
import net.davidtanzer.jevents.EventComponent;
import net.davidtanzer.jevents.EventComponents;

public class ECProvider<T extends EventComponent> implements Provider<T> {
	private final Class<T> componentClass;
	private MembersInjector<T> membersInjector;
	private Provider<EventComponents> eventComponentsProvider;

	private ECProvider(final Class<T> componentClass, final MembersInjector<T> membersInjector, final Provider<EventComponents> eventComponentsProvider) {
		this.componentClass = componentClass;
		this.membersInjector = membersInjector;
		this.eventComponentsProvider = eventComponentsProvider;
	}

	public static <C extends EventComponent> Provider<C> forComponent(final Class<C> componentClass, final MembersInjector<C> membersInjector, final Provider<EventComponents> eventComponentsProvider) {
		return new ECProvider<>(componentClass, membersInjector, eventComponentsProvider);
	}

	@Override
	public T get() {
		T component = eventComponentsProvider.get().createComponent(componentClass);
		membersInjector.injectMembers(component);
		return component;
	}

}
