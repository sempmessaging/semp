package org.sempmessaging.sempd.persistence.fs.io.lowlevel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.assertNotNull;

public class DirectoriesTest {
	private Directories directories;
	private FileSystemTester fileSystemTester;
	private Path basePath;

	@Before
	public void setup() throws Exception {
		directories = new Directories();
		fileSystemTester = new FileSystemTester();

		basePath = fileSystemTester.basePath();
	}

	@After
	public void teardown() throws Exception {
		fileSystemTester.teardown();
	}

	@Test(expected = PathDoesNotExistException.class)
	public void openDirectoryThrowsExceptionWhenDirectoryDoesNotExist() {
		directories.openDirectory(basePath.resolve("nonExistentDirectory"));
	}

	@Test(expected = PathIsNotADirectoryException.class)
	public void openDirectoryThrowsExceptionWhenPathExistsButIsNotADirectory() throws IOException {
		fileSystemTester.createFile("someFile");
		directories.openDirectory(basePath.resolve("someFile"));
	}

	@Test
	public void returnsDirectoryHandleWhenDirectoryExists() throws IOException {
		fileSystemTester.createDirectory("someDirectory");

		DirectoryHandle directoryHandle = directories.openDirectory(basePath.resolve("someDirectory"));

		assertNotNull(directoryHandle);
	}
}