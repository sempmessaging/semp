package org.sempmessaging.sempd.protocol.requests.serverinfo;

import org.junit.Before;
import org.junit.Test;
import org.sempmessaging.libsemp.key.PublicVerificationKey;
import org.sempmessaging.libsemp.request.RequestId;
import org.sempmessaging.sempd.core.serverkeys.PublicVerificationKeys;
import org.sempmessaging.sempd.protocol.io.ResponseData;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class PublicVerificationKeysToResponseDataTranslatorTest {

	private PublicVerificationKeyTranslator keyTranslator;
	private PublicVerificationKeysToResponseDataTranslator translator;

	@Before
	public void setup() throws Exception {
		keyTranslator = mock(PublicVerificationKeyTranslator.class);
		translator = new PublicVerificationKeysToResponseDataTranslator(keyTranslator);
	}

	@Test
	public void alwaysCreatesANonNullResponse() {
		ResponseData response = translator.toResponse(mock(PublicVerificationKeys.class));

		assertNotNull(response);
	}

	@Test
	public void addsKeyDataListToResponse() {
		ResponseData response = translator.toResponse(mock(PublicVerificationKeys.class));
		assumeNotNull(response);

		assertTrue(response.asMap().containsKey("ServerPublicVerificationKeys"));
		assertTrue(response.asMap().get("ServerPublicVerificationKeys") instanceof List);
	}

	@Test
	public void usesKeyTranslatorToTranslateIndividualKeys() {
		PublicVerificationKey key = mock(PublicVerificationKey.class);
		PublicVerificationKeyResponseData translatedKey = new PublicVerificationKeyResponseData();
		translatedKey.setRequestId(new RequestId("translatedKey"));
		when(keyTranslator.toResponse(any(PublicVerificationKey.class))).thenReturn(translatedKey);

		PublicVerificationKeys keys = mock(PublicVerificationKeys.class);
		doAnswer((invocation) -> {
			((Consumer<PublicVerificationKey>) invocation.getArguments()[0]).accept(key);
			return null;
		}).when(keys).forEach(any());

		ResponseData response = translator.toResponse(keys);
		assumeNotNull(response);
		assumeTrue(response.asMap().containsKey("ServerPublicVerificationKeys"));
		assumeTrue(response.asMap().get("ServerPublicVerificationKeys") instanceof List);

		List<Map<String, Object>> responseKeysList = (List<Map<String, Object>>) response.asMap().get("ServerPublicVerificationKeys");
		assertEquals(1, responseKeysList.size());
		Map<String, Object> keyData = responseKeysList.get(0);
		assertEquals("translatedKey", keyData.get("RequestId"));
	}
}