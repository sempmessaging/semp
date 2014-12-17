package org.sempmessaging.sempd.persistence.fs.serverkeys;

import com.google.inject.Provider;
import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.sempmessaging.libsemp.key.PublicVerificationKey;
import org.sempmessaging.libsemp.key.translator.JsonMapToPublicVerificationKeyTranslator;
import org.sempmessaging.sempd.core.serverkeys.PublicVerificationKeys;
import org.sempmessaging.sempd.core.serverkeys.VerificationKeysFactory;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class VerificationKeysCompilerTest {
	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	private VerificationKeysCompiler verificationKeysCompiler;
	private VerificationKeysFactory verificationKeysFactory;
	private Provider<JsonMapToPublicVerificationKeyTranslator> keyTranslatorProvider;

	@Before
	public void setup() throws Exception {
		verificationKeysCompiler = EventComponents.createComponent(VerificationKeysCompiler.class);

		verificationKeysFactory = mock(VerificationKeysFactory.class);
		verificationKeysCompiler.setVerificationKeysFactory(verificationKeysFactory);

		keyTranslatorProvider = mock(Provider.class);
		verificationKeysCompiler.setVerificationKeyTranslatorProvider(keyTranslatorProvider);
	}

	@Test
	public void finishCompilingKeysSendsNonNullVerificationKeysWhenNoKeyWasRead() {
		PublicVerificationKeys expectedVerificationKeys = mock(PublicVerificationKeys.class);
		when(verificationKeysFactory.newVerificationKeys(any())).thenReturn(expectedVerificationKeys);
		eventTestRule.subscribeMandatory(verificationKeysCompiler, verificationKeysCompiler.verificationKeysReadyEvent(), keys -> assertNotNull(keys));

		verificationKeysCompiler.finishCompilingKeys();
	}

	@Test
	public void finishCompilingKeysSendsEmptyVerificationKeysWhenNoKeyWasRead() {
		verificationKeysCompiler.finishCompilingKeys();

		ArgumentCaptor<List> keysListCaptor = ArgumentCaptor.forClass(List.class);
		verify(verificationKeysFactory).newVerificationKeys(keysListCaptor.capture());

		assertTrue(keysListCaptor.getValue().isEmpty());
	}

	@Test
	public void passesJsonMapFromReaderToKeyTranslator() throws Exception {
		JsonMapToPublicVerificationKeyTranslator translator = mock(JsonMapToPublicVerificationKeyTranslator.class);
		when(keyTranslatorProvider.get()).thenReturn(translator);

		Reader reader = new StringReader("{ \"Key\" = \"Value\" }");

		ArgumentCaptor<Map> jsonMapCaptor = ArgumentCaptor.forClass(Map.class);

		verificationKeysCompiler.readKeyFrom(reader);

		verify(translator).toServerPublicKey(jsonMapCaptor.capture());
		assertEquals("Value", jsonMapCaptor.getValue().get("Key"));
	}

	@Test
	public void addsReadVerificationKeyToKeysList() {
		JsonMapToPublicVerificationKeyTranslator translator = mock(JsonMapToPublicVerificationKeyTranslator.class);
		PublicVerificationKey expectedKey = mock(PublicVerificationKey.class);
		when(translator.toServerPublicKey(any())).thenReturn(expectedKey);
		when(keyTranslatorProvider.get()).thenReturn(translator);

		Reader reader = new StringReader("{ \"Key\" = \"Value\" }");

		verificationKeysCompiler.readKeyFrom(reader);
		verificationKeysCompiler.finishCompilingKeys();

		ArgumentCaptor<List> keysListCaptor = ArgumentCaptor.forClass(List.class);
		verify(verificationKeysFactory).newVerificationKeys(keysListCaptor.capture());

		assertEquals(1, keysListCaptor.getValue().size());
		assertSame(expectedKey, keysListCaptor.getValue().get(0));
	}
}