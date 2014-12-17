package net.davidtanzer.jevents;

public class EventResultFuture<T> {
	private final EventResultTranslator<T> translator;
	private boolean done;
	private T value;

	public EventResultFuture(final EventResultTranslator<T> translator) {
		this.translator = translator;
	}

	public boolean isDone() {
		return done;
	}

	public T get() {
		return value;
	}

	void handleEvent(final Object[] args) {
		value = translator.translate(args);
		done = true;
	}
}
