package net.davidtanzer.jevents;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class EventComponent {
	private HashMap<String, Object> events = new HashMap<>();
	private HashMap<Object, Object> eventHandlers = new HashMap<>();

	<T> T getEvent(final String eventName, final Class<T> eventType) {
		if(!eventType.isInterface()) {
			throw new IllegalStateException("Event type \""+eventType+"\" must be an interface.");
		}

		if(!events.containsKey(eventName)) {
			createEvent(eventName, eventType);
		}
		Object event = events.get(eventName);

		return (T) event;
	}

	private <T> void createEvent(final String eventName, final Class<T> eventType) {
		Object event = Proxy.newProxyInstance(eventType.getClassLoader(), new Class[] { eventType }, new DummyEventInvocationHandler());
		events.put(eventName, event);

		Object eventWithHandlers = Proxy.newProxyInstance(eventType.getClassLoader(), new Class[] { eventType }, new EventInvocationHandler());
		eventHandlers.put(event, eventWithHandlers);
	}

	public <T> void subscribe(final T event, final T eventHandler) {
		Object eventWithHandlers = eventHandlers.get(event);
		EventInvocationHandler invocationHandler = (EventInvocationHandler) Proxy.getInvocationHandler(eventWithHandlers);
		invocationHandler.registerSubscriber(eventHandler);
	}

	protected <T> T send(final T event) {
		return (T) eventHandlers.get(event);
	}

	public <T, E> EventResultFuture<T> futureFor(final E event, final EventResultTranslator<T> translator) {
		EventResultFuture<T> eventResultFuture = new EventResultFuture<>(translator);

		Object eventWithHandlers = eventHandlers.get(event);
		EventInvocationHandler invocationHandler = (EventInvocationHandler) Proxy.getInvocationHandler(eventWithHandlers);
		invocationHandler.registerWaitingFuture(eventResultFuture);

		return eventResultFuture;
	}

	private static class DummyEventInvocationHandler implements InvocationHandler {
		@Override
		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
			if(method.getName().equals("equals")) {
				return proxy == args[0];
			}
			if(method.getName().equals("hashCode")) {
				return hashCode();
			}
			throw new IllegalStateException("Can not call a method on an event object. Use this object to subscribeMandatory(...) or send(...) events.");
		}
	}

	private static class EventInvocationHandler implements InvocationHandler {
		private List<Object> subscribers = new ArrayList<>();
		private List<EventResultFuture<?>> waitingFutures = new ArrayList<>();

		@Override
		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
			for(Object subscriber : subscribers) {
				method.invoke(subscriber, args);
			}

			for(EventResultFuture waitingFuture : waitingFutures) {
				waitingFuture.handleEvent(args);
			}

			waitingFutures.clear();

			return null;
		}

		public <T> void registerSubscriber(final T eventHandler) {
			subscribers.add(eventHandler);
		}

		public <T> void registerWaitingFuture(final EventResultFuture<?> future) {
			waitingFutures.add(future);
		}
	}
}
