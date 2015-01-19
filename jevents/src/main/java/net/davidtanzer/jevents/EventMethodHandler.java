package net.davidtanzer.jevents;

import java.lang.reflect.Method;

public interface EventMethodHandler {
	Object handle(Object self, Method method);
}
