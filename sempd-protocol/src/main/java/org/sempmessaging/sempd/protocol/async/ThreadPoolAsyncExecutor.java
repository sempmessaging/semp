package org.sempmessaging.sempd.protocol.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolAsyncExecutor implements AsyncExecutor {
	private ExecutorService executorService;

	@Override
	public void execute(final Runnable runnable) {
		executorService.execute(runnable);
	}

	@Override
	public void start() {
		//FIXME the thread pool size should be configurable
		executorService = Executors.newFixedThreadPool(32);
	}

	@Override
	public void shutdown() {
		executorService.shutdown();
		while(!executorService.isTerminated()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				//We just keep waiting...
			}
		}
	}
}
