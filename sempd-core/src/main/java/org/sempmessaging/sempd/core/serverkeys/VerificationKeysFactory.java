package org.sempmessaging.sempd.core.serverkeys;

import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.key.PublicVerificationKey;

import java.util.List;

public class VerificationKeysFactory {
	public PublicVerificationKeys newVerificationKeys(final List<PublicVerificationKey> keysList) {
		Args.notNull(keysList, "keysList");
		return new PublicVerificationKeys(keysList);
	}
}
