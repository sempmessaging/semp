package org.sempmessaging.libsemp.request.serverpublickeys;

import org.sempmessaging.libsemp.request.RequestError;

public class ServerSentNoKeys extends RequestError {
	public ServerSentNoKeys(final ServerName serverName) {
		super("Server \""+serverName+"\" sent no keys.");
	}
}
