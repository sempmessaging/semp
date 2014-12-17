package org.sempmessaging.libsemp.roundrobin;

import com.google.inject.Inject;
import org.sempmessaging.libsemp.arguments.Args;

public class RoundRobinThread extends Thread {
	private boolean running = true;
	private RoundRobin roundRobin;

	private Object shutdownMonitor = new Object();

	@Inject
	public RoundRobinThread(RoundRobin roundRobin) {
		Args.notNull(roundRobin, "roundRobin");
		this.roundRobin = roundRobin;
	}

	@Override
	public void run() {
		runRoundRobin();
		notifyShutdown();
	}

	private void notifyShutdown() {
		synchronized (shutdownMonitor) {
			shutdownMonitor.notify();
		}
	}

	private void runRoundRobin() {
		while (running) {
			roundRobin.runOneRound();
			sleepALittle();
		}
	}

	private void sleepALittle() {
		try {
			sleep(10);
		} catch (InterruptedException e) {
			//That's OK, we just run the next round.
		}
	}

	public void shutdown() {
		this.running = false;
		waitForCleanShutdown();
	}

	private void waitForCleanShutdown() {
		synchronized (shutdownMonitor) {
			try {
				shutdownMonitor.wait();
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}
	}
}
