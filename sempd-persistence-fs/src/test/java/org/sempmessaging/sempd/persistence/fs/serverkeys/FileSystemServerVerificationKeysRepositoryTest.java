package org.sempmessaging.sempd.persistence.fs.serverkeys;

import com.google.inject.Provider;
import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.EventResultFuture;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.sempmessaging.datastore.fs.FileSystemAccess;
import org.sempmessaging.datastore.fs.FileSystemDataStore;
import org.sempmessaging.datastore.fs.FinishedEvent;
import org.sempmessaging.datastore.fs.ReadingFileEvent;
import org.sempmessaging.datastore.fs.operation.ReadEachFileOperation;
import org.sempmessaging.datastore.fs.operation.ReadEachFileOperationComponent;
import org.sempmessaging.sempd.core.serverkeys.PublicVerificationKeys;

import java.io.Reader;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class FileSystemServerVerificationKeysRepositoryTest {
	private FileSystemAccess fileSystemAccess;
	private FileSystemServerVerificationKeysRepository verificationKeysRepository;
	private VerificationKeysCompiler keysCompiler;
	private EventResultFuture verificationKeysResultFuture;
	private Provider<VerificationKeysCompiler> keysCompilerProvider;
	private FileSystemDataStore dataStore;
	private ReadEachFileOperation readEachFileOperation;

	@Before
	public void setup() throws Exception {
		readEachFileOperation = mock(ReadEachFileOperation.class);

		keysCompiler = mock(VerificationKeysCompiler.class);
		keysCompilerProvider = mock(Provider.class);
		when(keysCompilerProvider.get()).thenReturn(keysCompiler);

		verificationKeysResultFuture = mock(EventResultFuture.class);
		when(keysCompiler.futureFor(eq(keysCompiler.verificationKeysReadyEvent()), any())).thenReturn(verificationKeysResultFuture);

		fileSystemAccess = mock(FileSystemAccess.class);
		dataStore = mock(FileSystemDataStore.class);
		when(fileSystemAccess.dataStoreFor(any())).thenReturn(dataStore);

		Provider<ReadEachFileOperation> provider = mock(Provider.class);
		when(provider.get()).thenReturn(readEachFileOperation);
		verificationKeysRepository = new FileSystemServerVerificationKeysRepository(fileSystemAccess, keysCompilerProvider, provider);
	}

	@Test
	public void allVerificationKeysSetsUpEachFileReaderToSendReadingFileEventToKeysCompiler() {
		verificationKeysRepository.allVerificationKeys();

		ArgumentCaptor<?> handlerCaptor = ArgumentCaptor.forClass(Object.class);
		verify(readEachFileOperation, atLeastOnce()).subscribe(any(), handlerCaptor.capture());

		ReadingFileEvent readingFileEvent = null;
		for(Object o : handlerCaptor.getAllValues()) {
			if(o instanceof ReadingFileEvent) {
				readingFileEvent = (ReadingFileEvent) o;
			}
		}

		Reader reader = mock(Reader.class);
		readingFileEvent.readingFile(reader);
		verify(keysCompiler).readKeyFrom(eq(reader));
	}

	@Test
	public void allVerificationKeysSetsUpEachFileReaderToSendFinishedEventToKeysCompiler() {
		verificationKeysRepository.allVerificationKeys();

		ArgumentCaptor<FinishedEvent> handlerCaptor = ArgumentCaptor.forClass(FinishedEvent.class);
		verify(readEachFileOperation, atLeastOnce()).subscribe(any(), handlerCaptor.capture());

		FinishedEvent finishedEvent = null;
		for(Object o : handlerCaptor.getAllValues()) {
			if(o instanceof FinishedEvent) {
				finishedEvent = (FinishedEvent) o;
			}
		}
		finishedEvent.finished();
		verify(keysCompiler).finishCompilingKeys();
	}

	@Test
	public void allVerificationKeysStartsEachFileReader() {
		verificationKeysRepository.allVerificationKeys();

		verify(dataStore).performFileSystemOperations(readEachFileOperation);
	}

	@Test
	public void allVerificationKeysReturnsCompiledKeys() {
		PublicVerificationKeys expectedVerificationKeys = mock(PublicVerificationKeys.class);
		when(verificationKeysResultFuture.get()).thenReturn(expectedVerificationKeys);

		PublicVerificationKeys verificationKeys = verificationKeysRepository.allVerificationKeys();

		assertSame(expectedVerificationKeys, verificationKeys);
	}
}
