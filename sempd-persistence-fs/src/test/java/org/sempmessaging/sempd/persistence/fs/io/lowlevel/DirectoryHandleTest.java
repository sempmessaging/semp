package org.sempmessaging.sempd.persistence.fs.io.lowlevel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DirectoryHandleTest {

	private Directories directories;
	private FileSystemTester fileSystemTester;

	@Before
	public void setup() throws Exception {
		directories = new Directories();
		fileSystemTester = new FileSystemTester();
	}

	@After
	public void teardown() throws IOException {
		fileSystemTester.teardown();
	}

	@Test
	public void forEachFileCallsProcessorForEachFileInDirectory() throws IOException {
		fileSystemTester.createDirectory("someDir");
		fileSystemTester.createFile("someDir/file1");
		fileSystemTester.createFile("someDir/file2");

		DirectoryHandle directoryHandle = directories.openDirectory(fileSystemTester.basePath().resolve("someDir"));

		FileHandleProcessor processor = mock(FileHandleProcessor.class);
		directoryHandle.forEachFile(processor);

		verify(processor, times(2)).processFile(any());
	}
}