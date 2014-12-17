package org.sempmessaging.libsemp.roundrobin;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class RoundRobinTest {
	private RoundRobin roundrobin;

	@Before
	public void setup() {
		roundrobin = new RoundRobin();
	}

	@Test
	public void callsRegisteredRunnableInEachRound() {
		Runnable runnable = mock(Runnable.class);
		roundrobin.register(runnable);
		//It will only be registered after the next round.
		roundrobin.runOneRound();

		roundrobin.runOneRound();
		roundrobin.runOneRound();
		verify(runnable, times(2)).run();
	}

	@Test
	public void doesNotCallRunnableAfterUnregistered() {
		Runnable runnable = mock(Runnable.class);
		roundrobin.register(runnable);
		roundrobin.unregister(runnable);
		//It will only be registered after the next round.
		roundrobin.runOneRound();

		roundrobin.runOneRound();
		verify(runnable, never()).run();
	}
}
