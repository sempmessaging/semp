package org.sempmessaging.libsemp.request.serverinfo;

import org.sempmessaging.libsemp.request.RequestData;

public class GetServerInfoRequestData extends RequestData {
	public static final String REQUEST_JSON_IDENTIFIER = "GetServerInfo";

	public void setServerName(final ServerName serverName) {
		set(REQUEST_JSON_IDENTIFIER, serverName.value());
	}
}
