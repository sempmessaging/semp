package net.davidtanzer.jevents.testing;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class EventInvocationHandler implements InvocationHandler {
	private Object eventHandler;
	private boolean handled;

	public <T> EventInvocationHandler(final T eventHandler) {
		this.eventHandler = eventHandler;
	}

	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		handled = true;
		return method.invoke(eventHandler, args);
	}

	public boolean isHandled() {
		return handled;
	}

	public String eventName() {
		return eventHandler.getClass().getInterfaces()[0].getName();
	}
}
