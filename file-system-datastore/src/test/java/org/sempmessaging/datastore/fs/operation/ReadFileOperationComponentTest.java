package org.sempmessaging.datastore.fs.operation;

import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.sempmessaging.datastore.fs.FileSystemDataStore;
import org.sempmessaging.datastore.fs.lowlevel.Directories;
import org.sempmessaging.datastore.fs.lowlevel.FileHandle;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReadFileOperationComponentTest {
	@Rule
	public EventTestRule eventTestRule = new EventTestRule();
	private ReadFileOperationComponent readFileOperation;
	private Directories directories;

	@Before
	public void setup() {
		readFileOperation = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(ReadFileOperationComponent.class);

		directories = mock(Directories.class);
		readFileOperation.setDirectories(directories);
	}

	@Test
	public void sendsReadingEventWhenFileCanBeOpenedCorrectly() throws IOException {
		Reader expectedReader = mock(Reader.class);
		FileHandle fileHandle = mock(FileHandle.class);
		when(fileHandle.openReader()).thenReturn(expectedReader);
		when(directories.openFile(any())).thenReturn(fileHandle);

		eventTestRule.subscribeMandatory(readFileOperation, readFileOperation.readingFileEvent(), (reader) -> assertSame(expectedReader, reader));

		readFileOperation.readFile(mock(Path.class));
		readFileOperation.performOperationIn(basePath());
	}

	@Test
	public void closesTheOpenedReader() throws IOException {
		Reader expectedReader = mock(Reader.class);
		FileHandle fileHandle = mock(FileHandle.class);
		when(fileHandle.openReader()).thenReturn(expectedReader);
		when(directories.openFile(any())).thenReturn(fileHandle);

		readFileOperation.readFile(mock(Path.class));
		readFileOperation.performOperationIn(basePath());

		verify(expectedReader).close();
	}

	@Test @Ignore
	public void sendsErrorEventWhenFileCannotBeOpenedCorrectly() {
		fail("Implement me");
	}

	@Test @Ignore
	public void sendsErrorEventWhenExceptionOccursDuringReading() {
		fail("Implement me");
	}

	private FileSystemDataStore.BasePath basePath() {
		FileSystemDataStore.BasePath basePath = mock(FileSystemDataStore.BasePath.class);

		Path path = mock(Path.class);
		when(path.resolve(any(Path.class))).thenReturn(path);

		when(basePath.path()).thenReturn(path);
		return basePath;
	}
}