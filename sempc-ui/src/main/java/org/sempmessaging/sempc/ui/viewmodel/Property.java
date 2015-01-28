package org.sempmessaging.sempc.ui.viewmodel;

import org.sempmessaging.libsemp.arguments.Args;

public class Property<T> {
	private T value;
	private AbstractViewModel owner;

	Property(final AbstractViewModel owner) {
		Args.notNull(owner, "owner");
		this.owner = owner;
	}

	public void set(final T value) {
		this.value = value;
		owner.propertyChanged(this);
	}

	public T get() {
		return value;
	}
}
