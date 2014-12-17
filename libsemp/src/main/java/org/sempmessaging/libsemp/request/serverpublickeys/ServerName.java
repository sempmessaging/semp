package org.sempmessaging.libsemp.request.serverpublickeys;

import org.sempmessaging.libsemp.arguments.Args;
import net.davidtanzer.value.StringValue;

public class ServerName extends StringValue {
	public ServerName(final String value) {
		super(value);
		Args.notEmpty(value, "value");
	}

	private ServerName() {
		super();
	}
}
