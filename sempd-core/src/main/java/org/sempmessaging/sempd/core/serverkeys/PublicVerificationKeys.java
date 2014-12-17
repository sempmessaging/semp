package org.sempmessaging.sempd.core.serverkeys;

import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.key.PublicVerificationKey;

import java.util.List;
import java.util.function.Consumer;

public class PublicVerificationKeys {
	private List<PublicVerificationKey> keysList;

	PublicVerificationKeys(final List<PublicVerificationKey> keysList) {
		Args.notNull(keysList, "keysList");
		this.keysList = keysList;
	}

	public void forEach(Consumer<PublicVerificationKey> consumer) {
		keysList.forEach(consumer::accept);
	}
}
