package net.davidtanzer.jevents;

public interface EventResultTranslator<T> {
	T translate(Object... values);
}
