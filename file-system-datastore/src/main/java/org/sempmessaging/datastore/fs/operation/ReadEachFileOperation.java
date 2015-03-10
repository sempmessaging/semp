package org.sempmessaging.datastore.fs.operation;

import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventProvider;
import org.sempmessaging.datastore.fs.FileSystemOperation;
import org.sempmessaging.datastore.fs.FinishedEvent;
import org.sempmessaging.datastore.fs.ReadingFileEvent;

import java.nio.file.Path;

public interface ReadEachFileOperation extends FileSystemOperation, EventProvider {
	@Event
	ReadingFileEvent readingFileEvent();
	@Event
	FinishedEvent finishedReadingEvent();

	void readFromRelativePath(Path verification_keys);
}
