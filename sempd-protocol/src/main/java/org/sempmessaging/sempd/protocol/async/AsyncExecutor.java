package org.sempmessaging.sempd.protocol.async;

public interface AsyncExecutor {
	void execute(final Runnable runnable);
	void start();
	void shutdown();
}
