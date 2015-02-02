package org.sempmessaging.sempd.protocol.requests.serverinfo;

import org.junit.Before;
import org.junit.Test;
import org.sempmessaging.libsemp.key.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeNotNull;

public class PublicVerificationKeyTranslatorTest {
	private PublicVerificationKeyTranslator translator;
	private PublicVerificationKey keyData;

	@Before
	public void setup() {
		translator = new PublicVerificationKeyTranslator();
		keyData = createKeyData();
	}

	@Test
	public void alwaysReturnsAValue() {
		PublicVerificationKey key = createKeyData();

		PublicVerificationKeyResponseData response = translator.toResponse(key);

		assertNotNull(response);
	}

	@Test
	public void translatesKeyIdCorrectly() {
		translatesCorrectly("KeyId", keyData.keyId().value());
	}

	@Test
	public void translatesPublicKeyDataCorrectly() {
		translatesCorrectly("PublicKey", keyData.publicKeyData().value());
	}

	@Test
	public void translatesObsoleteByCorrectly() {
		translatesCorrectly("ObsoleteBy", keyData.obsoleteBy().value());
	}

	@Test
	public void translatesPreviousKeyCorrectly() {
		translatesCorrectly("PreviousKeyId", keyData.previousKey().value());
	}

	@Test
	public void translatesSignatureCorrectly() {
		translatesCorrectly("Signature", keyData.signature().value());
	}

	private void translatesCorrectly(final String field, final String value) {
		PublicVerificationKeyResponseData response = translator.toResponse(keyData);
		assumeNotNull(response);

		assertEquals(value, response.asMap().get(field));
	}

	private PublicVerificationKey createKeyData() {
		return new PublicVerificationKey(new KeyId("theKeyId"), new PublicKeyData("publicKeyData"), new ObsoleteByKeyId("obsoleteByKeyId"),
					new PreviousKeyId("previousKeyId"), new Signature("signature"));
	}
}