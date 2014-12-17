package org.sempmessaging.sempd.core.serverkeys;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerPublicVerificationKeysServiceTest {
	private ServerPublicVerificationKeysService verificationKeysService;
	private ServerVerificationKeysRepository verificationKeysRepository;

	@Before
	public void setup() throws Exception {
		verificationKeysRepository = mock(ServerVerificationKeysRepository.class);
		verificationKeysService = new ServerPublicVerificationKeysService(verificationKeysRepository);
	}

	@Test
	public void retrievesVerificationKeysFromRepository() {
		PublicVerificationKeys expectedResult = mock(PublicVerificationKeys.class);
		when(verificationKeysRepository.allVerificationKeys()).thenReturn(expectedResult);

		PublicVerificationKeys result = verificationKeysService.listPublicVerificationKeys();

		assertSame(expectedResult, result);
	}
}