package org.sempmessaging.libsemp.roundrobin;

import com.google.inject.Singleton;
import org.sempmessaging.libsemp.arguments.Args;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class RoundRobin {
	private List<Runnable> runnables = new ArrayList<>();
	private List<Runnable> runnablesToRegister = new ArrayList<>();
	private List<Runnable> runnablesToUnregister = new ArrayList<>();

	public void runOneRound() {
		for(Runnable r : runnables) {
			r.run();
		}

		List<Runnable> registerNow = runnablesToRegister;
		runnablesToRegister = new ArrayList<>();
		runnables.addAll(registerNow);

		List<Runnable> unregisterNow = runnablesToUnregister;
		runnablesToUnregister = new ArrayList<>();
		runnables.removeAll(unregisterNow);
	}

	public void register(final Runnable runnable) {
		Args.notNull(runnable, "runnable");
		runnablesToRegister.add(runnable);
	}

	public void unregister(final Runnable runnable) {
		Args.notNull(runnable, "runnable");

		runnablesToUnregister.add(runnable);
	}
}
