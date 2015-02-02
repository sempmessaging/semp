package org.sempmessaging.sempd.protocol.requests.serverinfo;

import org.junit.Before;
import org.junit.Test;
import org.sempmessaging.sempd.core.serverkeys.PublicVerificationKeys;
import org.sempmessaging.sempd.core.serverkeys.ServerPublicVerificationKeysService;
import org.sempmessaging.sempd.protocol.io.ResponseData;

import java.util.Map;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class GetServerInfoHandlerTest {
	private GetServerInfoHandler requestHandler;
	private ServerPublicVerificationKeysService keysService;
	private PublicVerificationKeysToResponseDataTranslator keysTranslator;

	@Before
	public void setup() {
		keysService = mock(ServerPublicVerificationKeysService.class);
		keysTranslator = mock(PublicVerificationKeysToResponseDataTranslator.class);
		requestHandler = new GetServerInfoHandler(keysService, keysTranslator);
	}

	@Test
	public void usesKeysServiceToGetPublicVerificationKeys() {
		requestHandler.safelyGetResponseFor(mock(Map.class));

		verify(keysService).listPublicVerificationKeys();
	}

	@Test
	public void usesKeysResponseTranslatorToTranslateServerKeysToResponseData() {
		PublicVerificationKeys publicVerificationKeys = mock(PublicVerificationKeys.class);
		when(keysService.listPublicVerificationKeys()).thenReturn(publicVerificationKeys);
		ResponseData expectedResponseData = mock(ResponseData.class);
		when(keysTranslator.toResponse(eq(publicVerificationKeys))).thenReturn(expectedResponseData);

		ResponseData actualResponseData = requestHandler.safelyGetResponseFor(mock(Map.class));

		assertSame(expectedResponseData, actualResponseData);
	}
}
