package org.sempmessaging.libsemp.request.serverpublickeys;

import org.sempmessaging.libsemp.request.RequestData;

public class GetServerPublicVerificationKeysRequestData extends RequestData {
	public void setServerName(final ServerName serverName) {
		set("GetServerPublicVerificationKeys", serverName.value());
	}
}
