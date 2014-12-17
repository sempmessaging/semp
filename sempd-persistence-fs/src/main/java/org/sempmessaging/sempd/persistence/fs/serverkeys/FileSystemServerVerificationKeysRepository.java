package org.sempmessaging.sempd.persistence.fs.serverkeys;

import com.google.inject.Inject;
import com.google.inject.Provider;
import net.davidtanzer.jevents.EventResultFuture;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempd.core.serverkeys.PublicVerificationKeys;
import org.sempmessaging.sempd.core.serverkeys.ServerVerificationKeysRepository;
import org.sempmessaging.sempd.persistence.fs.io.EachFileReader;
import org.sempmessaging.sempd.persistence.fs.io.FileSystemReader;

public class FileSystemServerVerificationKeysRepository implements ServerVerificationKeysRepository {
	private final FileSystemReader fileSystemReader;
	private Provider<VerificationKeysCompiler> keysCompilerFactory;

	@Inject
	public FileSystemServerVerificationKeysRepository(final FileSystemReader fileSystemReader, final Provider<VerificationKeysCompiler> keysCompilerProvider) {
		Args.notNull(fileSystemReader, "fileSystemReader");
		Args.notNull(keysCompilerProvider, "keysCompilerProvider");

		this.fileSystemReader = fileSystemReader;
		this.keysCompilerFactory = keysCompilerProvider;
	}

	@Override
	public PublicVerificationKeys allVerificationKeys() {
		EachFileReader eachFileReader = fileSystemReader.eachFileReader("server/verification_keys");
		VerificationKeysCompiler keysCompiler = keysCompilerFactory.get();

		eachFileReader.subscribe(eachFileReader.readingFileEvent(), keysCompiler::readKeyFrom);
		eachFileReader.subscribe(eachFileReader.finishedReadingEvent(), keysCompiler::finishCompilingKeys);

		EventResultFuture<PublicVerificationKeys> keysFuture = keysCompiler.futureFor(keysCompiler.verificationKeysReadyEvent(),
				args -> (PublicVerificationKeys) args[0]);
		eachFileReader.readEachFile();

		return keysFuture.get();
	}
}
