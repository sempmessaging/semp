package org.sempmessaging.datastore.fs.operation;

import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.sempmessaging.datastore.fs.FileSystemDataStore;
import org.sempmessaging.datastore.fs.lowlevel.Directories;
import org.sempmessaging.datastore.fs.lowlevel.DirectoryHandle;
import org.sempmessaging.datastore.fs.lowlevel.FileHandle;
import org.sempmessaging.datastore.fs.lowlevel.FileHandleProcessor;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ReadEachFileOperationTest {
	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	private ReadEachFileOperationComponent readEachFileOperation;
	private Directories directories;

	@Before
	public void setup() {
		readEachFileOperation = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(ReadEachFileOperationComponent.class);

		directories = mock(Directories.class);
		readEachFileOperation.setDirectories(directories);
	}

	@Test
	public void opensDirectoryWithCorrectPath() {
		DirectoryHandle directoryHandle = mock(DirectoryHandle.class);
		when(directories.openDirectory(any())).thenReturn(directoryHandle);

		Path relativePath = mock(Path.class);
		FileSystemDataStore.BasePath basePath = basePath();
		Path pathToRead = mock(Path.class);
		when(basePath.path().resolve(eq(relativePath))).thenReturn(pathToRead);

		readEachFileOperation.readFromRelativePath(relativePath);
		readEachFileOperation.performOperationIn(basePath);

		verify(directories).openDirectory(eq(pathToRead));
	}

	@Test
	public void sendsFinishedEventAfterReadingNoFiles() {
		DirectoryHandle directoryHandle = mock(DirectoryHandle.class);
		when(directories.openDirectory(any())).thenReturn(directoryHandle);
		eventTestRule.subscribeMandatory(readEachFileOperation, readEachFileOperation.finishedReadingEvent(), () -> {});

		readEachFileOperation.readFromRelativePath(mock(Path.class));
		readEachFileOperation.performOperationIn(basePath());
	}

	@Test
	public void sendsReadingFileEventForEachFileInDirectory() throws IOException {
		DirectoryHandle directoryHandle = mock(DirectoryHandle.class);
		when(directories.openDirectory(any())).thenReturn(directoryHandle);

		Reader expectedReader = mock(Reader.class);
		FileHandle fileHandle = mock(FileHandle.class);
		when(fileHandle.openReader()).thenReturn(expectedReader);
		doAnswer((invocation) -> {
			((FileHandleProcessor) invocation.getArguments()[0]).processFile(fileHandle);
			return null;
		}).when(directoryHandle).forEachFile(any());

		eventTestRule.subscribeMandatory(readEachFileOperation, readEachFileOperation.readingFileEvent(), (reader) -> assertSame(expectedReader, reader));

		readEachFileOperation.readFromRelativePath(mock(Path.class));
		readEachFileOperation.performOperationIn(basePath());
	}

	@Test
	public void closesEachOpenedReader() throws IOException {
		DirectoryHandle directoryHandle = mock(DirectoryHandle.class);
		when(directories.openDirectory(any())).thenReturn(directoryHandle);

		Reader expectedReader = mock(Reader.class);
		FileHandle fileHandle = mock(FileHandle.class);
		when(fileHandle.openReader()).thenReturn(expectedReader);
		doAnswer((invocation) -> {
			((FileHandleProcessor) invocation.getArguments()[0]).processFile(fileHandle);
			return null;
		}).when(directoryHandle).forEachFile(any());

		readEachFileOperation.readFromRelativePath(mock(Path.class));
		readEachFileOperation.performOperationIn(basePath());

		verify(expectedReader).close();
	}

	private FileSystemDataStore.BasePath basePath() {
		FileSystemDataStore.BasePath basePath = mock(FileSystemDataStore.BasePath.class);
		Path path = mock(Path.class);
		when(basePath.path()).thenReturn(path);
		return basePath;
	}

}