package org.sempmessaging.sempd.persistence.fs.serverkeys;

import com.google.inject.Provider;
import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.EventResultFuture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.sempmessaging.sempd.core.serverkeys.PublicVerificationKeys;
import org.sempmessaging.sempd.persistence.fs.io.EachFileReader;
import org.sempmessaging.sempd.persistence.fs.io.FileSystemReader;
import org.sempmessaging.sempd.persistence.fs.io.FinishedEvent;
import org.sempmessaging.sempd.persistence.fs.io.ReadingFileEvent;

import java.io.Reader;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class FileSystemServerVerificationKeysRepositoryTest {
	private FileSystemReader fileSystemReader;
	private FileSystemServerVerificationKeysRepository verificationKeysRepository;
	private EachFileReader eachFileReader;
	private VerificationKeysCompiler keysCompiler;
	private EventResultFuture verificationKeysResultFuture;
	private Provider<VerificationKeysCompiler> keysCompilerProvider;

	@Before
	public void setup() throws Exception {
		eachFileReader = spy(EventComponents.createComponent(EachFileReader.class));
		doAnswer((invocationOnMock) -> null).when(eachFileReader).readEachFile();
		fileSystemReader = mock(FileSystemReader.class);
		when(fileSystemReader.eachFileReader(anyString())).thenReturn(eachFileReader);

		keysCompiler = mock(VerificationKeysCompiler.class);
		keysCompilerProvider = mock(Provider.class);
		when(keysCompilerProvider.get()).thenReturn(keysCompiler);

		verificationKeysResultFuture = mock(EventResultFuture.class);
		when(keysCompiler.futureFor(eq(keysCompiler.verificationKeysReadyEvent()), any())).thenReturn(verificationKeysResultFuture);

		verificationKeysRepository = new FileSystemServerVerificationKeysRepository(fileSystemReader, keysCompilerProvider);
	}

	@Test
	public void allVerificationKeysSetsUpEachFileReaderToSendReadingFileEventToKeysCompiler() {
		verificationKeysRepository.allVerificationKeys();

		ArgumentCaptor<ReadingFileEvent> handlerCaptor = ArgumentCaptor.forClass(ReadingFileEvent.class);
		verify(eachFileReader).subscribe(eq(eachFileReader.readingFileEvent()), handlerCaptor.capture());

		Reader reader = mock(Reader.class);
		handlerCaptor.getValue().readingFile(reader);
		verify(keysCompiler).readKeyFrom(eq(reader));
	}

	@Test
	public void allVerificationKeysSetsUpEachFileReaderToSendFinishedEventToKeysCompiler() {
		verificationKeysRepository.allVerificationKeys();

		ArgumentCaptor<FinishedEvent> handlerCaptor = ArgumentCaptor.forClass(FinishedEvent.class);
		verify(eachFileReader).subscribe(eq(eachFileReader.finishedReadingEvent()), handlerCaptor.capture());

		handlerCaptor.getValue().finished();
		verify(keysCompiler).finishCompilingKeys();
	}

	@Test
	public void allVerificationKeysStartsEachFileReader() {
		verificationKeysRepository.allVerificationKeys();

		verify(eachFileReader).readEachFile();
	}

	@Test
	public void allVerificationKeysReturnsCompiledKeys() {
		PublicVerificationKeys expectedVerificationKeys = mock(PublicVerificationKeys.class);
		when(verificationKeysResultFuture.get()).thenReturn(expectedVerificationKeys);

		PublicVerificationKeys verificationKeys = verificationKeysRepository.allVerificationKeys();

		assertSame(expectedVerificationKeys, verificationKeys);
	}
}
