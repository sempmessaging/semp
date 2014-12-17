package org.sempmessaging.sempd.protocol.requests.serverpublickeys;

import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.key.*;
import org.sempmessaging.sempd.protocol.io.ResponseData;

public class PublicVerificationKeyResponseData extends ResponseData {
	public void setKeyId(final KeyId keyId) {
		Args.notNull(keyId, "keyId");
		set("KeyId", keyId.value());
	}

	public void setPublicKey(final PublicKeyData publicKeyData) {
		Args.notNull(publicKeyData, "publicKeyData");
		set("PublicKey", publicKeyData.value());
	}

	public void setObsoleteBy(final ObsoleteByKeyId obsoleteByKeyId) {
		Args.notNull(obsoleteByKeyId, "obsoleteByKeyId");
		set("ObsoleteBy", obsoleteByKeyId.value());
	}

	public void setPreviousKeyId(final PreviousKeyId previousKeyId) {
		Args.notNull(previousKeyId, "previousKeyId");
		set("PreviousKeyId", previousKeyId.value());
	}

	public void setSignature(final Signature signature) {
		Args.notNull(signature, "signature");
		set("Signature", signature.value());
	}
}
