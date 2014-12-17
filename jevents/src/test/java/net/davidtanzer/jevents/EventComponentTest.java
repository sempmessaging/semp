package net.davidtanzer.jevents;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;

public class EventComponentTest {
	private EventComponentUnderTest component;
	private boolean firstSubscriberCalled;
	private boolean secondSubscriberCalled;

	@Before
	public void setup() {
		component = EventComponents.createComponent(EventComponentUnderTest.class);
	}

	@Test
	public void getEventDoesNotReturnNull() {
		DoSomething event = component.getEvent("something", DoSomething.class);

		assertNotNull(event);
	}

	@Test
	public void getEventAlwaysReturnsTheSameEvent() {
		DoSomething event1 = component.getEvent("something", DoSomething.class);
		DoSomething event2 = component.getEvent("something", DoSomething.class);

		assumeNotNull(event1);
		assumeNotNull(event2);
		assertSame(event1, event2);
	}

	@Test(expected = IllegalStateException.class)
	public void eventMethodsCannotBeCalledDirectly() {
		component.doSomethingEvent().doIt();
	}

	@Test
	public void sendCallsAllSubscribersOfAnEvent() {
		component.subscribe(component.doSomethingEvent(), () -> firstSubscriberCalled = true);
		component.subscribe(component.doSomethingEvent(), () -> secondSubscriberCalled = true);

		component.send(component.doSomethingEvent()).doIt();

		assertTrue(firstSubscriberCalled);
		assertTrue(secondSubscriberCalled);
	}

	@Test
	public void waitForDoesNotReturnNull() {
		EventResultFuture<String> future = component.futureFor(component.returnSomething(), (something) -> (String) something[0]);

		assertNotNull(future);
	}

	@Test
	public void futureForFutureIsNotDoneWhenEventWasNotSent() {
		EventResultFuture<String> future = component.futureFor(component.returnSomething(), (something) -> (String) something[0]);
		assumeNotNull(future);

		assertFalse(future.isDone());
	}

	@Test
	public void futureForFutureIsDoneWhenEventWasSent() {
		EventResultFuture<String> future = component.futureFor(component.returnSomething(), (something) -> something[0] + "Foo");
		assumeNotNull(future);

		component.send(component.returnSomething()).returnSomething("FooBar");

		assertTrue(future.isDone());
	}

	@Test
	public void futureForFutureHasCorrectValueWhenEventWasSent() throws ExecutionException, InterruptedException {
		EventResultFuture<String> future = component.futureFor(component.returnSomething(), (something) -> something[0] + "Foo");
		assumeNotNull(future);

		component.send(component.returnSomething()).returnSomething("FooBar");
		assumeTrue(future.isDone());

		assertEquals("FooBarFoo", future.get());
	}

	@Test
	public void futureForFutureGetsValueForOnlyOneEvent() throws ExecutionException, InterruptedException {
		EventResultFuture<String> future = component.futureFor(component.returnSomething(), (something) -> something[0] + "Foo");
		assumeNotNull(future);

		component.send(component.returnSomething()).returnSomething("FooBar");
		component.send(component.returnSomething()).returnSomething("Second Event");
		assumeTrue(future.isDone());

		assertEquals("FooBarFoo", future.get());
	}

	public static abstract class EventComponentUnderTest extends EventComponent {
		@Event
		public abstract DoSomething doSomethingEvent();

		@Event
		public abstract ReturnSomething returnSomething();
	}
}
