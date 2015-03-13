package org.sempmessaging.datastore.fs.lowlevel;

import java.nio.file.Path;

public class PathIsNotAFileException extends RuntimeException {
	public PathIsNotAFileException(final Path path) {
		super("Path is not a regular file: \""+path.toAbsolutePath().toString()+"\".");
	}
}
