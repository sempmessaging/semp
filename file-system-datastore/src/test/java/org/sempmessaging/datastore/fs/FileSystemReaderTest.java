package org.sempmessaging.datastore.fs;

import com.google.inject.Provider;
import org.junit.Before;
import org.junit.Test;
import org.sempmessaging.sempd.persistence.fs.FileSystemPersistenceConfiguration;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class FileSystemReaderTest {

	private FileSystemReader fileSystemReader;
	private Provider<EachFileReader> eachFileReaderProvider;
	private FileSystemPersistenceConfiguration fileSystemPersistenceConfiguration;

	@Before
	public void setup() throws Exception {
		eachFileReaderProvider = mock(Provider.class);
		fileSystemPersistenceConfiguration = mock(FileSystemPersistenceConfiguration.class);

		fileSystemReader = new FileSystemReader(fileSystemPersistenceConfiguration, eachFileReaderProvider);
	}

	@Test
	public void eachFileReaderReturnsNewInstanceFromProvider() {
		EachFileReader expectedEachFileReader = mock(EachFileReader.class);
		when(eachFileReaderProvider.get()).thenReturn(expectedEachFileReader);
		when(fileSystemPersistenceConfiguration.basePath()).thenReturn(mock(Path.class));

		EachFileReader actualEachFileReader = fileSystemReader.eachFileReader("anyPath");

		assertSame(expectedEachFileReader, actualEachFileReader);
	}

	@Test
	public void eachFileReaderPassesPathWithCorrectBasePathToTheReader() {
		FileSystem fileSystem = FileSystems.getDefault();
		EachFileReader eachFileReader = mock(EachFileReader.class);
		when(eachFileReaderProvider.get()).thenReturn(eachFileReader);
		when(fileSystemPersistenceConfiguration.basePath()).thenReturn(fileSystem.getPath("base/path"));

		fileSystemReader.eachFileReader("path/to/read");

		Path expectedPath = fileSystem.getPath("base/path", "path/to/read");
		verify(eachFileReader).readFromPath(expectedPath);
	}
}