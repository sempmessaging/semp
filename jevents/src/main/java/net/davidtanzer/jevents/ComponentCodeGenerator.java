package net.davidtanzer.jevents;

public interface ComponentCodeGenerator {
	<T extends EventComponent> T createEventComponentImplementation(Class<T> componentClass, EventMethodHandler eventMethodHandler);
}
