package net.davidtanzer.jevents;

public interface EventProvider {
	<T> void subscribe(final T event, final T eventHandler);
}
