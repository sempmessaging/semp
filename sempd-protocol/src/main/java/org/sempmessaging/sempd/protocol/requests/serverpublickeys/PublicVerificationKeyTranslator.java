package org.sempmessaging.sempd.protocol.requests.serverpublickeys;

import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.key.PublicVerificationKey;

public class PublicVerificationKeyTranslator {
	public PublicVerificationKeyResponseData toResponse(final PublicVerificationKey key) {
		Args.notNull(key, "key");

		PublicVerificationKeyResponseData responseData = new PublicVerificationKeyResponseData();
		responseData.setKeyId(key.keyId());
		responseData.setPublicKey(key.publicKeyData());
		responseData.setObsoleteBy(key.obsoleteBy());
		responseData.setPreviousKeyId(key.previousKey());
		responseData.setSignature(key.signature());

		return responseData;
	}
}
