package org.sempmessaging.datastore.fs;

import net.davidtanzer.jevents.EventComponents;
import testing.EventTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.sempmessaging.sempd.persistence.fs.io.lowlevel.Directories;
import org.sempmessaging.sempd.persistence.fs.io.lowlevel.DirectoryHandle;
import org.sempmessaging.sempd.persistence.fs.io.lowlevel.FileHandle;
import org.sempmessaging.sempd.persistence.fs.io.lowlevel.FileHandleProcessor;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class EachFileReaderTest {
	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	private EachFileReader eachFileReader;
	private Directories directories;

	@Before
	public void setup() {
		eachFileReader = EventComponents.createComponent(EachFileReader.class);

		directories = mock(Directories.class);
		eachFileReader.setDirectories(directories);
	}

	@Test
	public void opensDirectoryHandleWhenSettingPathToRead() {
		Path pathToRead = mock(Path.class);

		eachFileReader.readFromPath(pathToRead);

		verify(directories).openDirectory(eq(pathToRead));
	}

	@Test
	public void sendsFinishedEventAfterReadingNoFiles() {
		DirectoryHandle directoryHandle = mock(DirectoryHandle.class);
		when(directories.openDirectory(any())).thenReturn(directoryHandle);
		eventTestRule.subscribeMandatory(eachFileReader, eachFileReader.finishedReadingEvent(), () -> {});

		eachFileReader.readFromPath(mock(Path.class));
		eachFileReader.readEachFile();
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

		eventTestRule.subscribeMandatory(eachFileReader, eachFileReader.readingFileEvent(), (reader) -> assertSame(expectedReader, reader));

		eachFileReader.readFromPath(mock(Path.class));
		eachFileReader.readEachFile();
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

		eachFileReader.readFromPath(mock(Path.class));
		eachFileReader.readEachFile();

		verify(expectedReader).close();
	}
}
