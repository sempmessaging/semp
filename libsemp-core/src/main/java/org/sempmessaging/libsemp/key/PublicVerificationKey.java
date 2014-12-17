package org.sempmessaging.libsemp.key;

public class PublicVerificationKey {
	private final KeyId keyId;
	private final PublicKeyData publicKeyData;
	private final ObsoleteByKeyId obsoleteBy;
	private final PreviousKeyId previousKey;
	private final Signature signature;

	public PublicVerificationKey(final KeyId keyId, final PublicKeyData publicKeyData, final ObsoleteByKeyId obsoleteBy, final PreviousKeyId previousKey, final Signature signature) {
		this.keyId = keyId;
		this.publicKeyData = publicKeyData;
		this.obsoleteBy = obsoleteBy;
		this.previousKey = previousKey;
		this.signature = signature;
	}

	public KeyId keyId() {
		return keyId;
	}

	public PublicKeyData publicKeyData() {
		return publicKeyData;
	}

	public ObsoleteByKeyId obsoleteBy() {
		return obsoleteBy;
	}

	public PreviousKeyId previousKey() {
		return previousKey;
	}

	public Signature signature() {
		return signature;
	}
}
