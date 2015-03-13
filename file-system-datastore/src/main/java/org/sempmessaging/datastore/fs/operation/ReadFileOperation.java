package org.sempmessaging.datastore.fs.operation;

import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventProvider;
import org.sempmessaging.datastore.fs.FileSystemOperation;
import org.sempmessaging.datastore.fs.ReadingFileEvent;

public interface ReadFileOperation extends FileSystemOperation, EventProvider {
	@Event
	ReadingFileEvent readingFileEvent();
}
