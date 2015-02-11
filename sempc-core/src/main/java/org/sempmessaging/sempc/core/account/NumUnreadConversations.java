package org.sempmessaging.sempc.core.account;

import net.davidtanzer.value.IntegerValue;

public class NumUnreadConversations extends IntegerValue {
	public NumUnreadConversations(final int value) {
		super(value);
	}

	private NumUnreadConversations() {
		super();
	}
}
