package org.sempmessaging.datastore.fs.lowlevel;

import java.nio.file.Files;
import java.nio.file.Path;

public class Directories {
	public DirectoryHandle openDirectory(final Path path) {
		if(!Files.exists(path)) {
			throw new PathDoesNotExistException(path);
		}
		if(!Files.isDirectory(path)) {
			throw new PathIsNotADirectoryException(path);
		}
		return new DirectoryHandle(path);
	}

	public FileHandle openFile(final Path path) {
		if(!Files.exists(path)) {
			throw new PathDoesNotExistException(path);
		}
		if(!Files.isRegularFile(path)) {
			throw new PathIsNotAFileException(path);
		}
		return new FileHandle(path);
	}
}
