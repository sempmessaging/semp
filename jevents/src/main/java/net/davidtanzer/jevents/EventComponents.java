package net.davidtanzer.jevents;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventComponents {
	public static <T extends EventComponent> T createComponent(final Class<T> componentClass) {
		return createEventComponentImplementation(componentClass);
	}

	private static <T extends EventComponent> T createEventComponentImplementation(final Class<T> componentClass) {
		ProxyFactory factory = new ProxyFactory();
		factory.setSuperclass(componentClass);

		factory.setFilter(method -> method.isAnnotationPresent(Event.class));
		MethodHandler handler = (self, thisMethod, proceed, args) -> handleEventMethod(self, thisMethod, proceed, args);

		try {
			return (T) factory.create(new Class<?>[0], new Object[0], handler);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	}

	private static Object handleEventMethod(final Object self, final Method thisMethod, final Method proceed, final Object[] args) {
		assert self instanceof EventComponent : "Because event component implementations are only created for EventComponents";

		return ((EventComponent) self).getEvent(thisMethod.getName(), thisMethod.getReturnType());
	}
}
