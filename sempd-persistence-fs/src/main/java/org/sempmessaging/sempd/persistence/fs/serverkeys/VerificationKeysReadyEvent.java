package org.sempmessaging.sempd.persistence.fs.serverkeys;

import org.sempmessaging.sempd.core.serverkeys.PublicVerificationKeys;

public interface VerificationKeysReadyEvent {
	void verificationKeysReady(PublicVerificationKeys verificationKeys);
}
