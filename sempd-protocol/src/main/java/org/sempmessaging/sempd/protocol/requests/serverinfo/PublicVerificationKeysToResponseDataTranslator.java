package org.sempmessaging.sempd.protocol.requests.serverinfo;

import com.google.inject.Inject;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempd.core.serverkeys.PublicVerificationKeys;
import org.sempmessaging.sempd.protocol.io.ResponseData;

import java.util.ArrayList;

public class PublicVerificationKeysToResponseDataTranslator {
	private PublicVerificationKeyTranslator keyTranslator;

	@Inject
	public PublicVerificationKeysToResponseDataTranslator(final PublicVerificationKeyTranslator keyTranslator) {
		Args.notNull(keyTranslator, "keyTranslator");

		this.keyTranslator = keyTranslator;
	}

	public ResponseData toResponse(final PublicVerificationKeys keys) {
		Args.notNull(keys, "keys");
		ServerInfoResponseData responseData = new ServerInfoResponseData();

		ArrayList<PublicVerificationKeyResponseData> translatedKeys = new ArrayList<>();
		keys.forEach((key) -> translatedKeys.add(keyTranslator.toResponse(key)));

		responseData.setServerPublicVerificationKeys(translatedKeys);

		return responseData;
	}
}
