package org.sempmessaging.libsemp.request.serverinfo;

import com.google.inject.Inject;
import com.google.inject.Provider;
import net.davidtanzer.jevents.Event;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.key.PublicVerificationKey;
import org.sempmessaging.libsemp.key.translator.JsonMapToPublicVerificationKeyTranslator;
import org.sempmessaging.libsemp.request.Request;
import org.sempmessaging.libsemp.request.RequestData;
import org.sempmessaging.libsemp.server.ServerInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GetServerInfoRequest extends Request {
	public static final String SERVER_PUBLIC_VERIFICATION_KEYS_JSON_IDENTIFIER = "ServerPublicVerificationKeys";
	public static final String SERVER_PUBLIC_ENCRYPTION_KEY_IDENTIFIER = "ServerPublicEncryptionKey";

	private ServerName serverName;
	private Provider<JsonMapToPublicVerificationKeyTranslator> keyTranslatorProvider;

	@Event
	public abstract ServerInformationReceivedEvent serverInformationReceivedEvent();

	@Override
	protected RequestData createRequestData() {
		if(serverName == null) {
			throw new IllegalStateException("serverName must not be null.");
		}
		GetServerInfoRequestData requestData = new GetServerInfoRequestData();
		requestData.setServerName(serverName);
		return requestData;
	}

	@Override
	public void processResponse(final Map<String, Object> jsonResponse) {
		Args.notNull(jsonResponse, "jsonResponse");

		if(responseContainsKeysList(jsonResponse) && keysListContainsKeys(jsonResponse)) {
			try {
				List<PublicVerificationKey> keysList = createKeysListFrom((List) jsonResponse.get(SERVER_PUBLIC_VERIFICATION_KEYS_JSON_IDENTIFIER));

				ServerInformation serverInformation = new ServerInformation(keysList);
				send(serverInformationReceivedEvent()).serverInformationReceived(serverInformation);
			} catch(KeyListContainsNonMapData e) {
				send(errorDuringRequest()).error(e);
			}
		} else {
			send(errorDuringRequest()).error(new ServerSentNoKeys(serverName));
		}
	}

	private List<PublicVerificationKey> createKeysListFrom(final List rawKeysList) throws KeyListContainsNonMapData {
		List<PublicVerificationKey> result = new ArrayList<>();

		for(Object o : rawKeysList) {
			if(o instanceof Map) {
				result.add(keyTranslatorProvider.get().toServerPublicKey((Map<String, Object>) o));
			} else {
				throw new KeyListContainsNonMapData(o);
			}
		}

		return result;
	}

	private boolean keysListContainsKeys(final Map<String, Object> jsonResponse) {
		return ((List) jsonResponse.get(SERVER_PUBLIC_VERIFICATION_KEYS_JSON_IDENTIFIER)).size() > 0;
	}

	private boolean responseContainsKeysList(final Map<String, Object> jsonResponse) {
		return jsonResponse.containsKey(SERVER_PUBLIC_VERIFICATION_KEYS_JSON_IDENTIFIER) &&
				jsonResponse.get(SERVER_PUBLIC_VERIFICATION_KEYS_JSON_IDENTIFIER) instanceof List<?>;
	}

	public void requestKeysForServer(final ServerName serverName) {
		Args.notNull(serverName, "serverName");
		Args.setOnce(this.serverName, "serverName");

		this.serverName = serverName;
	}

	@Inject
	void setPublicKeyTranslatorProvider(final Provider<JsonMapToPublicVerificationKeyTranslator> keyTranslatorProvider) {
		Args.notNull(keyTranslatorProvider, "keyTranslatorProvider");
		Args.setOnce(this.keyTranslatorProvider, "keyTranslatorProvider");

		this.keyTranslatorProvider = keyTranslatorProvider;
	}
}
