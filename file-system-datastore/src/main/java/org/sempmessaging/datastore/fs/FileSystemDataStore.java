package org.sempmessaging.datastore.fs;

import org.sempmessaging.libsemp.arguments.Args;

import java.nio.file.Path;
import java.util.List;

public class FileSystemDataStore {
	private Path dataStoreBasePath;

	public synchronized void withBasePath(final Path dataStoreBasePath) {
		Args.notNull(dataStoreBasePath, "dataStoreBasePath");
		Args.setOnce(this.dataStoreBasePath, "dataStoreBasePath");

		this.dataStoreBasePath = dataStoreBasePath;
	}

	public synchronized void performFileSystemOperations(FileSystemOperation... operations) {
		Args.notNull(operations, "operations");

		for(FileSystemOperation operation : operations) {
			operation.performOperationIn(new BasePath(dataStoreBasePath));
		}
	}

	public static class BasePath {
		private final Path dataStoreBasePath;

		private BasePath(final Path dataStoreBasePath) {
			this.dataStoreBasePath = dataStoreBasePath;
		}

		public Path path() {
			return dataStoreBasePath;
		}
	}
}
