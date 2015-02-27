package org.sempmessaging.libsemp.server;

import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.key.PublicVerificationKey;

import java.util.List;

public class ServerInformation {
	private final List<PublicVerificationKey> publicKeys;

	public ServerInformation(final List<PublicVerificationKey> publicKeys) {
		Args.notNull(publicKeys, "publicKeys");

		this.publicKeys = publicKeys;
	}

	public List<PublicVerificationKey> publicKeys() {
		return publicKeys;
	}
}
