package org.sempmessaging.sempd.protocol.requests.serverpublickeys;

import org.sempmessaging.sempd.protocol.io.ResponseData;

import java.util.List;

public class ServerPublicVerificationKeysResponseData extends ResponseData {
	public void setServerPublicVerificationKeys(final List<PublicVerificationKeyResponseData> serverPublicVerificationKeys) {
		set("ServerPublicVerificationKeys", serverPublicVerificationKeys);
	}
}
