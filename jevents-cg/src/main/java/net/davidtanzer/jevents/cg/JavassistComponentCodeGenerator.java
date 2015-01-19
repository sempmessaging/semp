package net.davidtanzer.jevents.cg;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import net.davidtanzer.jevents.ComponentCodeGenerator;
import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventComponent;
import net.davidtanzer.jevents.EventMethodHandler;

import java.lang.reflect.InvocationTargetException;

public class JavassistComponentCodeGenerator implements ComponentCodeGenerator {
	@Override
	public <T extends EventComponent> T createEventComponentImplementation(final Class<T> componentClass, final EventMethodHandler eventMethodHandler) {
		ProxyFactory factory = new ProxyFactory();
		factory.setSuperclass(componentClass);

		factory.setFilter(method -> method.isAnnotationPresent(Event.class));
		MethodHandler handler = (self, thisMethod, proceed, args) -> eventMethodHandler.handle(self, thisMethod);

		try {
			return (T) factory.create(new Class<?>[0], new Object[0], handler);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	}
}
