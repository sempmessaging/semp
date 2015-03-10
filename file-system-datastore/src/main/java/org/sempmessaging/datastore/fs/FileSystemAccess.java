package org.sempmessaging.datastore.fs;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.sempmessaging.datastore.fs.value.Location;
import org.sempmessaging.libsemp.arguments.Args;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileSystemAccess {
	private final Path basePath;
	private final Map<Location, FileSystemDataStore> dataStores = new HashMap<>();
	private Provider<FileSystemDataStore> fileSystemDataStoreProvider;

	public FileSystemAccess(final Path basePath) {
		Args.notNull(basePath, "basePath");
		this.basePath = basePath;
	}

	public synchronized FileSystemDataStore dataStoreFor(final Location location) {
		Args.notNull(location, "location");

		registerDataStoreIfNecessary(location);
		return dataStores.get(location);
	}

	private void registerDataStoreIfNecessary(final Location location) {
		if(!dataStores.containsKey(location)) {
			assert fileSystemDataStoreProvider != null : "Data store provider must be set by dependency injection or test setup.";
			FileSystemDataStore dataStore = fileSystemDataStoreProvider.get();
			dataStore.withBasePath(basePath.resolve(location.value()));
			dataStores.put(location, dataStore);
		}
	}

	@Inject
	public void setFileSystemDataStoreProvider(Provider<FileSystemDataStore> fileSystemDataStoreProvider) {
		Args.notNull(fileSystemDataStoreProvider, "fileSystemDataStoreProvider");
		Args.setOnce(this.fileSystemDataStoreProvider, "fileSystemDataStoreProvider");

		this.fileSystemDataStoreProvider = fileSystemDataStoreProvider;
	}
}
