package org.sempmessaging.sempd.core.serverkeys;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ServerPublicVerificationKeysService {
	private ServerVerificationKeysRepository verificationKeysRepository;

	@Inject
	public ServerPublicVerificationKeysService(final ServerVerificationKeysRepository verificationKeysRepository) {
		this.verificationKeysRepository = verificationKeysRepository;
	}

	public PublicVerificationKeys listPublicVerificationKeys() {
		PublicVerificationKeys verificationKeys = verificationKeysRepository.allVerificationKeys();
		return verificationKeys;
	}
}
