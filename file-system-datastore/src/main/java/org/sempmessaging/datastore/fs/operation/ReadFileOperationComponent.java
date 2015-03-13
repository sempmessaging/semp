package org.sempmessaging.datastore.fs.operation;

import com.google.inject.Inject;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.datastore.fs.FileSystemDataStore;
import org.sempmessaging.datastore.fs.lowlevel.Directories;
import org.sempmessaging.datastore.fs.lowlevel.FileHandle;
import org.sempmessaging.libsemp.arguments.Args;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

public abstract class ReadFileOperationComponent extends EventComponent implements ReadFileOperation {
	private Directories directories;
	private Path relativeFilePath;

	@Override
	public void performOperationIn(final FileSystemDataStore.BasePath basePath) {
		Args.notNull(basePath, "basePath");

		final FileHandle file = openFileIn(basePath);
		safelyReadFile(file);
	}

	private FileHandle openFileIn(final FileSystemDataStore.BasePath basePath) {
		assert directories != null : "Directories must be set by dependency injection or test setup.";
		assert relativeFilePath != null : "Relative file path must not be null. Did you call readFile(...) before starting the operation?";

		final Path absolutePath = basePath.path().resolve(relativeFilePath);
		assert absolutePath != null : "Path does (probably) never return null when resolving paths...";

		return directories.openFile(absolutePath);
	}

	private void safelyReadFile(final FileHandle file) {
		try {
			readFile(file);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private void readFile(final FileHandle file) throws IOException {
		final Reader reader = file.openReader();
		send(readingFileEvent()).readingFile(reader);
		reader.close();
	}

	public void readFile(final Path relativeFilePath) {
		Args.notNull(relativeFilePath, "relativeFilePath");
		Args.setOnce(this.relativeFilePath, "relativeFilePath");

		this.relativeFilePath = relativeFilePath;
	}

	@Inject
	void setDirectories(final Directories directories) {
		Args.notNull(directories, "directories");
		Args.setOnce(this.directories, "directories");

		this.directories = directories;
	}
}
