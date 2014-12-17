package net.davidtanzer.jevents.testing;

import junit.framework.AssertionFailedError;
import net.davidtanzer.jevents.EventComponent;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class EventTestRule implements TestRule {
	private List<EventInvocationHandler> mandatoryInvocationHandlers = new ArrayList<>();

	public <T> void subscribeMandatory(final EventComponent component, final T event, final T eventHandler) {
		Class<? extends Object> eventType = event.getClass();
		Class<?> eventInterface = eventType.getInterfaces()[0];

		EventInvocationHandler eventInvocationHandler = new EventInvocationHandler(eventHandler);
		Object eventProxy = Proxy.newProxyInstance(eventInterface.getClassLoader(),
				new Class[] { eventInterface }, eventInvocationHandler);
		mandatoryInvocationHandlers.add(eventInvocationHandler);

		component.subscribe(event, eventProxy);
	}

	public Statement apply(Statement base, Description description) {
		return statement(base);
	}

	private Statement statement(final Statement base) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				try {
					base.evaluate();
					after();
				} catch(Exception e) {
					throw getRealCause(e);
				}
			}
		};
	}

	private Throwable getRealCause(final Exception e) {
		Throwable rootCause = e;
		while(rootCause.getCause() != null) {
			rootCause = rootCause.getCause();
		}

		if(rootCause instanceof AssertionError) {
			return rootCause;
		}
		return e;
	}

	private void after() {
		for(EventInvocationHandler handler : mandatoryInvocationHandlers) {
			assertTrue("Handler for \""+handler.eventName()+"\" must be handled", handler.isHandled());
		}
	}

	public void waitForEventsUntil(final long timeout) {
		//Busy waiting... Why not.
		long startTime = System.currentTimeMillis();
		boolean allHandled = false;
		while(!allHandled && System.currentTimeMillis() - startTime <= timeout) {
			allHandled = checkIfAllEventsWereHandled();

			sleepALittle();
		}

		if(!allHandled) {
			throw new AssertionFailedError("Not all events were handled!");
		}
	}

	private boolean checkIfAllEventsWereHandled() {
		boolean allHandled = true;
		for(EventInvocationHandler handler : mandatoryInvocationHandlers) {
			allHandled = allHandled && handler.isHandled();
		}
		return allHandled;
	}

	private void sleepALittle() {
		try {
			Thread.sleep(10L);
		} catch (InterruptedException e) {
		}
	}
}
