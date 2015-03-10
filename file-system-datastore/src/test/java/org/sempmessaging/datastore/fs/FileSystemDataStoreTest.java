package org.sempmessaging.datastore.fs;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FileSystemDataStoreTest {
	private FileSystemDataStore dataStore;
	private Path basePath;

	@Before
	public void setup() {
		dataStore = new FileSystemDataStore();
		basePath = mock(Path.class);
		dataStore.withBasePath(basePath);
	}

	@Test
	public void performFileSystemOperationExecutesAllGivenOperations() {
		FileSystemOperation operation1 = mock(FileSystemOperation.class);
		FileSystemOperation operation2 = mock(FileSystemOperation.class);

		dataStore.performFileSystemOperations(operation1, operation2);

		verify(operation1).performOperationIn(any(FileSystemDataStore.BasePath.class));
		verify(operation2).performOperationIn(any(FileSystemDataStore.BasePath.class));
	}
}