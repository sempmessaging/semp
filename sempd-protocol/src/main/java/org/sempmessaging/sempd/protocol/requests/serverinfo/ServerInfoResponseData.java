package org.sempmessaging.sempd.protocol.requests.serverinfo;

import org.sempmessaging.sempd.protocol.io.ResponseData;

import java.util.List;

public class ServerInfoResponseData extends ResponseData {
	public void setServerPublicVerificationKeys(final List<PublicVerificationKeyResponseData> serverPublicVerificationKeys) {
		set("ServerPublicVerificationKeys", serverPublicVerificationKeys);
	}
}
