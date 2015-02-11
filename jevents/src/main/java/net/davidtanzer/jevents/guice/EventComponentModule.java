package net.davidtanzer.jevents.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import net.davidtanzer.jevents.EventComponent;
import net.davidtanzer.jevents.EventComponents;

import java.lang.annotation.Annotation;

public abstract class EventComponentModule extends AbstractModule {
	protected <T extends EventComponent> com.google.inject.binder.ScopedBindingBuilder bindEventComponent(final Class<T> componentClass) {
		return bind(componentClass).toProvider(ECProvider.forComponent(componentClass, getMembersInjector(componentClass)));
	}

	protected <T extends EventComponent> com.google.inject.binder.ScopedBindingBuilder bindAnnotatedEventComponent(final Class<? extends Annotation> annotationType, final Class<T> componentClass) {
		return bind(componentClass)
				.annotatedWith(annotationType)
				.toProvider(ECProvider.forComponent(componentClass, getMembersInjector(componentClass)));
	}

	protected <T extends EventComponent> com.google.inject.binder.ScopedBindingBuilder bindAnnotatedEventComponent(final Annotation annotationType, final Class<T> componentClass) {
		return bind(componentClass)
				.annotatedWith(annotationType)
				.toProvider(ECProvider.forComponent(componentClass, getMembersInjector(componentClass)));
	}

	protected <T extends EventComponent> com.google.inject.binder.ScopedBindingBuilder bindAnnotatedEventComponent(final Annotation annotationType, final TypeLiteral typeLiteral) {
		return bind(typeLiteral)
				.annotatedWith(annotationType)
				.toProvider(ECProvider.forComponent(typeLiteral.getRawType(), getMembersInjector(typeLiteral.getRawType())));
	}
}
