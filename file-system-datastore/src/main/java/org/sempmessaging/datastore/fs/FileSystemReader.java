package org.sempmessaging.datastore.fs;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempd.persistence.fs.FileSystemPersistenceConfiguration;

import java.nio.file.Path;

public class FileSystemReader {
	private Provider<EachFileReader> eachFileReaderProvider;
	private FileSystemPersistenceConfiguration fileSystemPersistenceConfiguration;

	@Inject
	FileSystemReader(final FileSystemPersistenceConfiguration fileSystemPersistenceConfiguration, final Provider<EachFileReader> eachFileReaderProvider) {
		Args.notNull(fileSystemPersistenceConfiguration, "fileSystemPersistenceConfiguration");
		Args.notNull(eachFileReaderProvider, "eachFileReaderProvider");

		this.fileSystemPersistenceConfiguration = fileSystemPersistenceConfiguration;
		this.eachFileReaderProvider = eachFileReaderProvider;
	}

	public EachFileReader eachFileReader(final String path) {
		Args.notEmpty(path, "path");

		EachFileReader eachFileReader = eachFileReaderProvider.get();

		Path combinedPath = fileSystemPersistenceConfiguration.basePath().resolve(path);
		eachFileReader.readFromPath(combinedPath);

		return eachFileReader;
	}
}
