package org.sempmessaging.libsemp.request.serverpublickeys;

import org.sempmessaging.libsemp.key.PublicVerificationKey;

import java.util.List;

public interface PublicKeysReceivedEvent {
	void publicKeysReceived(List<PublicVerificationKey> publicKeys);
}
