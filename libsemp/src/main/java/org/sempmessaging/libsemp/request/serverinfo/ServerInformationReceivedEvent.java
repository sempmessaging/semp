package org.sempmessaging.libsemp.request.serverinfo;

import org.sempmessaging.libsemp.key.PublicVerificationKey;
import org.sempmessaging.libsemp.server.ServerInformation;

import java.util.List;

public interface ServerInformationReceivedEvent {
	void serverInformationReceived(ServerInformation serverInformation);
}
