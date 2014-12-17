package org.sempmessaging.sempd.persistence.fs.io.lowlevel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeNotNull;

public class FileHandleTest {

	private FileSystemTester fileSystemTester;

	@Before
	public void setup() throws Exception {
		fileSystemTester = new FileSystemTester();
	}

	@After
	public void teardown() throws IOException {
		fileSystemTester.teardown();
	}

	@Test
	public void openReaderDoesNotReturnNull() throws IOException {
		fileSystemTester.createFile("someFile", "someContent");
		FileHandle fileHandle = new FileHandle(fileSystemTester.basePath().resolve("someFile"));

		Reader reader = fileHandle.openReader();
		assertNotNull(reader);

		reader.close();
	}

	@Test
	public void openReaderOpensFileForReading() throws IOException {
		fileSystemTester.createFile("someFile", "someContent");
		FileHandle fileHandle = new FileHandle(fileSystemTester.basePath().resolve("someFile"));

		Reader reader = fileHandle.openReader();
		assumeNotNull(reader);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String fileContent = bufferedReader.readLine();

		assertEquals("someContent", fileContent);

		bufferedReader.close();
	}
}