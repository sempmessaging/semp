package org.sempmessaging.sempd.persistence.fs.io;

import com.google.inject.Inject;
import net.davidtanzer.jevents.Event;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempd.persistence.fs.io.lowlevel.Directories;
import org.sempmessaging.sempd.persistence.fs.io.lowlevel.DirectoryHandle;
import org.sempmessaging.sempd.persistence.fs.io.lowlevel.FileHandle;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

public abstract class EachFileReader extends EventComponent {
	private Directories directories;
	private DirectoryHandle directoryToReadFrom;

	@Event
	public abstract ReadingFileEvent readingFileEvent();

	@Event
	public abstract FinishedEvent finishedReadingEvent();

	public void readEachFile() {
		assert directoryToReadFrom != null : "Directory to read from must not be null. Did you call \"readFromPath(...)\" before this method?";

		try {
			directoryToReadFrom.forEachFile(this::readCurrentFile);
		} catch (IOException e) {
			throw new IllegalStateException("FIXME: Send error event!");
		}
		send(finishedReadingEvent()).finished();
	}

	private void readCurrentFile(final FileHandle fileHandle) {
		Args.notNull(fileHandle, "fileHandle");

		try {
			safelyReadCurrentFile(fileHandle);
		} catch (IOException e) {
			throw new IllegalStateException("FIXME: Send error event!");
		}
	}

	private void safelyReadCurrentFile(final FileHandle fileHandle) throws IOException {
		Reader reader = fileHandle.openReader();
		send(readingFileEvent()).readingFile(reader);
		reader.close();
	}

	public void readFromPath(final Path path) {
		Args.notNull(path, "path");
		Args.setOnce(this.directoryToReadFrom, "directoryToReadFrom");
		assert directories != null : "Directories must be set by dependency injection or test setup.";

		directoryToReadFrom = directories.openDirectory(path);
	}

	@Inject
	void setDirectories(final Directories directories) {
		Args.notNull(directories, "directories");
		Args.setOnce(this.directories, "directories");

		this.directories = directories;
	}
}
