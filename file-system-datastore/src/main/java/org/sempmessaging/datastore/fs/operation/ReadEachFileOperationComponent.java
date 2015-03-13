package org.sempmessaging.datastore.fs.operation;

import com.google.inject.Inject;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.datastore.fs.FileSystemDataStore;
import org.sempmessaging.datastore.fs.lowlevel.Directories;
import org.sempmessaging.datastore.fs.lowlevel.DirectoryHandle;
import org.sempmessaging.datastore.fs.lowlevel.FileHandle;
import org.sempmessaging.libsemp.arguments.Args;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

public abstract class ReadEachFileOperationComponent extends EventComponent implements ReadEachFileOperation {
	private Directories directories;
	private Path relativePath;

	@Override
	public void performOperationIn(final FileSystemDataStore.BasePath basePath) {
		assert directories != null : "Directories must be set by dependency injection or test setup.";
		Path absolutePath = basePath.path().resolve(relativePath);
		assert absolutePath != null : "Path does (probably) never return null when resolving paths...";

		DirectoryHandle directory = directories.openDirectory(absolutePath);
		//FIXME handle exceptions!

		try {
			directory.forEachFile(this::readCurrentFile);
			send(finishedReadingEvent()).finished();
		} catch (IOException e) {
			throw new IllegalStateException("FIXME: Send error event!");
		}
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

	public void readFromRelativePath(final Path relativePath) {
		Args.notNull(relativePath, "relativePath");
		Args.setOnce(this.relativePath, "relativePath");

		this.relativePath = relativePath;
	}

	@Inject
	void setDirectories(final Directories directories) {
		Args.notNull(directories, "directories");
		Args.setOnce(this.directories, "directories");

		this.directories = directories;
	}
}
