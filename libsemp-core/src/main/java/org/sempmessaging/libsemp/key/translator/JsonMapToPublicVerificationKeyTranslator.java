package org.sempmessaging.libsemp.key.translator;

import com.google.inject.Inject;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.key.*;
import org.sempmessaging.libsemp.translator.JsonMapTranslator;

import java.util.Map;

public class JsonMapToPublicVerificationKeyTranslator {
	private JsonMapTranslator mapTranslator;

	@Inject
	public JsonMapToPublicVerificationKeyTranslator(final JsonMapTranslator mapTranslator) {
		Args.notNull(mapTranslator, "mapTranslator");
		this.mapTranslator = mapTranslator;
	}

	public PublicVerificationKey toServerPublicKey(final Map<String, Object> jsonMap) {
		mapTranslator.readFrom(jsonMap);

		KeyId keyId = mapTranslator.read("KeyId", KeyId.class, new IllegalPublicKeyData("Server Public Key must contain a KeyId"));
		PublicKeyData keyData = mapTranslator.read("PublicKey", PublicKeyData.class, new IllegalPublicKeyData("Server Public Key must contain a PublicKey"));
		ObsoleteByKeyId obsoleteBy = mapTranslator.read("ObsoleteBy", ObsoleteByKeyId.class);
		PreviousKeyId previousKey = mapTranslator.read("PreviousKeyId", PreviousKeyId.class);
		Signature signature = mapTranslator.read("Signature", Signature.class);

		return new PublicVerificationKey(keyId, keyData, obsoleteBy, previousKey, signature);
	}
}
