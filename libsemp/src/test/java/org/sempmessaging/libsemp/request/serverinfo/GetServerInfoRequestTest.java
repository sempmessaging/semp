package org.sempmessaging.libsemp.request.serverinfo;

import com.google.inject.Provider;
import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.testing.EventTestRule;
import net.davidtanzer.value.SingleValue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.sempmessaging.libsemp.key.*;
import org.sempmessaging.libsemp.key.translator.JsonMapToPublicVerificationKeyTranslator;
import org.sempmessaging.libsemp.request.JSONSender;
import org.sempmessaging.libsemp.request.RequestId;
import org.sempmessaging.libsemp.translator.JsonMapTranslator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.sempmessaging.libsemp.PredicateMatcher.argMatches;

public class GetServerInfoRequestTest {
	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	private GetServerInfoRequest request;
	private JSONSender jsonSender;

	@Before
	public void setup() {
		request = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(GetServerInfoRequest.class);

		Provider<JsonMapToPublicVerificationKeyTranslator> keyTranslatorProvider = mock(Provider.class);
		when(keyTranslatorProvider.get()).thenAnswer((invocation) -> new JsonMapToPublicVerificationKeyTranslator(new JsonMapTranslator()));
		request.setPublicKeyTranslatorProvider(keyTranslatorProvider);

		request.setId(RequestId.newRandomRequestId());
		request.requestKeysForServer(new ServerName("semp.example.com"));

		jsonSender = mock(JSONSender.class);
		request.sendJsonTo(jsonSender);
	}

	@Test
	public void startSendsRequestDataToJsonSender() {
		request.start();

		verify(jsonSender).send(any(Map.class));
	}

	@Test
	public void startSendsRequestDataForCorrectServerToJsonSender() {
		request.start();

		verify(jsonSender).send(
				argMatches((m) -> "semp.example.com".equals(m.get("GetServerInfo")),
				"JSON Data must contain correct server name"));
	}

	@Test
	public void processResponseReportsAnErrorWhenServerSendsNoKeys() {
		Map<String, Object> jsonResponse = new HashMap<String, Object>() {{
			put("ServerPublicKeys", new ArrayList<>());
		}};

		eventTestRule.subscribeMandatory(request, request.errorDuringRequest(), (e) -> assertNotNull(e));
		request.processResponse(jsonResponse);
	}

	@Test
	public void processResponseSendsTwoPublicKeysWhenTheServerProvidesTheKeys() {
		Map<String, Object> jsonResponse = preparePublicKeysResponse();

		eventTestRule.subscribeMandatory(request, request.publicKeysReceivedEvent(), (keys) -> assertEquals(2, keys.size()));
		request.processResponse(jsonResponse);
	}

	@Test
	public void processResponseSendsCorrectPublicKeysWhenTheServerProvidesTheKeys() {
		Map<String, Object> jsonResponse = preparePublicKeysResponse();

		eventTestRule.subscribeMandatory(request, request.publicKeysReceivedEvent(), (keys) -> {
			assumeThat(keys.size(), is(2));

			assertKeyIs(keys.get(0), "KeyId1", "PublicKeyData1", "KeyId2");
			assertKeyIs(keys.get(1), "KeyId2", "PublicKeyData2", "KeyId1", "Signature2", "2014-05-31 13:14:15");
		});
		request.processResponse(jsonResponse);
	}

	private void assertKeyIs(final PublicVerificationKey key, final String keyId, final String keyData, final String previousKey, final String signature, final String validUntil) {
		assertEquals(new KeyId(keyId), key.keyId());
		assertEquals(new PublicKeyData(keyData), key.publicKeyData());
		assertEquals(new PreviousKeyId(previousKey), key.previousKey());
		assertEquals(new Signature(signature), key.signature());
	}

	private void assertKeyIs(final PublicVerificationKey key, final String keyId, final String keyData, final String obsoleteBy) {
		assertEquals(new KeyId(keyId), key.keyId());
		assertEquals(new PublicKeyData(keyData), key.publicKeyData());
		assertEquals(new ObsoleteByKeyId(obsoleteBy), key.obsoleteBy());

		assertEquals(SingleValue.empty(PreviousKeyId.class), key.previousKey());
		assertEquals(SingleValue.empty(Signature.class), key.signature());
	}

	private Map<String, Object> preparePublicKeysResponse() {
		return new HashMap<String, Object>() {{
			put(GetServerInfoRequest.SERVER_PUBLIC_KEYS_JSON_IDENTIFIER,  preparePublicKeysList());
		}};
	}

	private List<Map<String, Object>> preparePublicKeysList() {
		return new ArrayList<Map<String, Object>>() {{
			add(new HashMap<String, Object>() {{
				put("KeyId", "KeyId1");
				put("PublicKey", "PublicKeyData1");
				put("ObsoleteBy", "KeyId2");
			}});
			add(new HashMap<String, Object>() {{
				put("KeyId", "KeyId2");
				put("PublicKey", "PublicKeyData2");
				put("PreviousKeyId", "KeyId1");
				put("Signature", "Signature2");
				put("CacheValidUntil", "2014-05-31 13:14:15");
			}});
		}};
	}
}