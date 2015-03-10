package org.sempmessaging.datastore.fs;

import com.google.inject.Provider;
import org.junit.Before;
import org.junit.Test;
import org.sempmessaging.datastore.fs.value.Location;

import java.nio.file.Path;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileSystemAccessTest {
	private FileSystemAccess fileSystemAccess;
	private Provider<FileSystemDataStore> fileSystemDataStoreProvider;
	private Path basePath;

	@Before
	public void setup() {
		basePath = mock(Path.class);
		fileSystemAccess = new FileSystemAccess(basePath);

		fileSystemDataStoreProvider = mock(Provider.class);
		fileSystemAccess.setFileSystemDataStoreProvider(fileSystemDataStoreProvider);
	}

	@Test
	public void dataStoreForDoesNotReturnNullForValidLocation() {
		Location location = mock(Location.class);
		FileSystemDataStore fileSystemDataStore = mock(FileSystemDataStore.class);
		when(fileSystemDataStoreProvider.get()).thenReturn(fileSystemDataStore);

		FileSystemDataStore dataStore = fileSystemAccess.dataStoreFor(location);

		assertNotNull(dataStore);
	}

	@Test
	public void dataStoreForReturnsNewDataStoreForDifferentLocation() {
		FileSystemDataStore fileSystemDataStore1 = mock(FileSystemDataStore.class);
		FileSystemDataStore fileSystemDataStore2 = mock(FileSystemDataStore.class);
		when(fileSystemDataStoreProvider.get()).thenReturn(fileSystemDataStore1).thenReturn(fileSystemDataStore2);

		FileSystemDataStore dataStore1 = fileSystemAccess.dataStoreFor(mock(Location.class));
		FileSystemDataStore dataStore2 = fileSystemAccess.dataStoreFor(mock(Location.class));

		assumeNotNull(dataStore1);
		assumeNotNull(dataStore2);
		assertNotEquals(dataStore1, dataStore2);
	}

	@Test
	public void dataStoreForReturnsSameDataStoreForSameLocation() {
		FileSystemDataStore fileSystemDataStore1 = mock(FileSystemDataStore.class);
		FileSystemDataStore fileSystemDataStore2 = mock(FileSystemDataStore.class);
		when(fileSystemDataStoreProvider.get()).thenReturn(fileSystemDataStore1).thenReturn(fileSystemDataStore2);

		Location location = mock(Location.class);
		FileSystemDataStore dataStore1 = fileSystemAccess.dataStoreFor(location);
		FileSystemDataStore dataStore2 = fileSystemAccess.dataStoreFor(location);

		assumeNotNull(dataStore1);
		assumeNotNull(dataStore2);
		assertEquals(dataStore1, dataStore2);
	}

	@Test
	public void dataStoreForSetsBasePathOfNewDataStore() {
		Location location = new Location("foo/bar");
		FileSystemDataStore fileSystemDataStore = mock(FileSystemDataStore.class);
		when(fileSystemDataStoreProvider.get()).thenReturn(fileSystemDataStore);

		Path dataStoreBasePath = mock(Path.class);
		when(basePath.resolve(eq("foo/bar"))).thenReturn(dataStoreBasePath);

		FileSystemDataStore dataStore = fileSystemAccess.dataStoreFor(location);

		assumeNotNull(dataStore);
		verify(dataStore).withBasePath(eq(dataStoreBasePath));
	}
}