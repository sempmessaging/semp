package org.sempmessaging.sempd.persistence.fs.io.lowlevel;

import java.nio.file.Path;

public class PathIsNotADirectoryException extends RuntimeException {
	public PathIsNotADirectoryException(final Path path) {
		super("Path is not a directory: \""+path.toAbsolutePath().toString()+"\".");
	}
}
