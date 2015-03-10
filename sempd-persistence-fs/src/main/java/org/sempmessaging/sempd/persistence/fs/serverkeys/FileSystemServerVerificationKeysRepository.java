package org.sempmessaging.sempd.persistence.fs.serverkeys;

import com.google.inject.Inject;
import com.google.inject.Provider;
import net.davidtanzer.jevents.EventResultFuture;
import org.sempmessaging.datastore.fs.FileSystemAccess;
import org.sempmessaging.datastore.fs.FileSystemDataStore;
import org.sempmessaging.datastore.fs.operation.ReadEachFileOperation;
import org.sempmessaging.datastore.fs.operation.ReadEachFileOperationComponent;
import org.sempmessaging.datastore.fs.value.Location;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempd.core.serverkeys.PublicVerificationKeys;
import org.sempmessaging.sempd.core.serverkeys.ServerVerificationKeysRepository;

import java.nio.file.Paths;

public class FileSystemServerVerificationKeysRepository implements ServerVerificationKeysRepository {
	private final FileSystemAccess fileSystemAccess;
	private final Provider<ReadEachFileOperation> readEachFileOperationProvider;
	private final Provider<VerificationKeysCompiler> keysCompilerFactory;

	@Inject
	public FileSystemServerVerificationKeysRepository(final FileSystemAccess fileSystemAccess, final Provider<VerificationKeysCompiler> keysCompilerProvider, final Provider<ReadEachFileOperation> readEachFileOperationProvider) {
		Args.notNull(fileSystemAccess, "fileSystemAccess");
		Args.notNull(keysCompilerProvider, "keysCompilerProvider");
		Args.notNull(readEachFileOperationProvider, "readEachFileOperationProvider");

		this.fileSystemAccess = fileSystemAccess;
		this.keysCompilerFactory = keysCompilerProvider;
		this.readEachFileOperationProvider = readEachFileOperationProvider;
	}

	@Override
	public PublicVerificationKeys allVerificationKeys() {
		FileSystemDataStore dataStore = fileSystemAccess.dataStoreFor(new Location("server"));

		ReadEachFileOperation readEachFileOperation = readEachFileOperationProvider.get();
		readEachFileOperation.readFromRelativePath(Paths.get("verification_keys"));
		VerificationKeysCompiler keysCompiler = keysCompilerFactory.get();

		readEachFileOperation.subscribe(readEachFileOperation.readingFileEvent(), keysCompiler::readKeyFrom);
		readEachFileOperation.subscribe(readEachFileOperation.finishedReadingEvent(), keysCompiler::finishCompilingKeys);

		EventResultFuture<PublicVerificationKeys> keysFuture = keysCompiler.futureFor(keysCompiler.verificationKeysReadyEvent(),
				args -> (PublicVerificationKeys) args[0]);
		dataStore.performFileSystemOperations(readEachFileOperation);

		return keysFuture.get();
	}
}
