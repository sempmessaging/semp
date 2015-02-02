package org.sempmessaging.sempd.protocol.requests.serverinfo;

import com.google.inject.Inject;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempd.core.serverkeys.PublicVerificationKeys;
import org.sempmessaging.sempd.core.serverkeys.ServerPublicVerificationKeysService;
import org.sempmessaging.sempd.protocol.io.ResponseData;
import org.sempmessaging.sempd.protocol.requests.handler.OneTimeRequestHandler;

import java.util.Map;

public class GetServerInfoHandler extends OneTimeRequestHandler {
	private final ServerPublicVerificationKeysService keysService;
	private PublicVerificationKeysToResponseDataTranslator keysTranslator;

	@Inject
	public GetServerInfoHandler(final ServerPublicVerificationKeysService keysService, final PublicVerificationKeysToResponseDataTranslator keysTranslator) {
		Args.notNull(keysService, "keysService");
		Args.notNull(keysTranslator, "keysTranslator");

		this.keysService = keysService;
		this.keysTranslator = keysTranslator;
	}

	@Override
	protected ResponseData safelyGetResponseFor(final Map<String, Object> jsonData) {
		Args.notNull(jsonData, "jsonData");

		PublicVerificationKeys keys = keysService.listPublicVerificationKeys();
		ResponseData responseData = keysTranslator.toResponse(keys);

		return responseData;
	}
}
