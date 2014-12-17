package org.sempmessaging.sempd.persistence.fs.serverkeys;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Provider;
import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.libsemp.key.PublicVerificationKey;
import org.sempmessaging.libsemp.key.translator.JsonMapToPublicVerificationKeyTranslator;
import org.sempmessaging.sempd.core.serverkeys.PublicVerificationKeys;
import org.sempmessaging.sempd.core.serverkeys.VerificationKeysFactory;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class VerificationKeysCompiler extends EventComponent {
	private VerificationKeysFactory verificationKeysFactory;
	private List<PublicVerificationKey> keysList = new ArrayList<>();
	private Provider<JsonMapToPublicVerificationKeyTranslator> keyTranslatorProvider;

	@Event
	public abstract VerificationKeysReadyEvent verificationKeysReadyEvent();

	public void readKeyFrom(final Reader reader) {
		Args.notNull(reader, "reader");

		//FIXME handler all kinds of errors...
		Gson gson = new Gson();
		Map<String, Object> jsonMap = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());

		PublicVerificationKey verificationKey = keyTranslatorProvider.get().toServerPublicKey(jsonMap);

		keysList.add(verificationKey);
	}

	public void finishCompilingKeys() {
		assert verificationKeysFactory != null : "verificationKeysFactory must be set by DI or test setup.";

		PublicVerificationKeys verificationKeys = verificationKeysFactory.newVerificationKeys(keysList);

		send(verificationKeysReadyEvent()).verificationKeysReady(verificationKeys);
	}

	@Inject
	void setVerificationKeysFactory(final VerificationKeysFactory verificationKeysFactory) {
		Args.notNull(verificationKeysFactory, "verificationKeysFactory");
		Args.setOnce(this.verificationKeysFactory, "verificationKeysFactory");

		this.verificationKeysFactory = verificationKeysFactory;
	}

	@Inject
	void setVerificationKeyTranslatorProvider(final Provider<JsonMapToPublicVerificationKeyTranslator> keyTranslatorProvider) {
		Args.notNull(keyTranslatorProvider, "keyTranslatorProvider");
		Args.setOnce(this.keyTranslatorProvider, "keyTranslatorProvider");

		this.keyTranslatorProvider = keyTranslatorProvider;
	}
}
