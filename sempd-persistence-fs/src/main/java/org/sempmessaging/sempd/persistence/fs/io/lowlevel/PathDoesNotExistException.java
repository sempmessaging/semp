package org.sempmessaging.sempd.persistence.fs.io.lowlevel;

import java.nio.file.Path;

public class PathDoesNotExistException extends RuntimeException {
	public PathDoesNotExistException(final Path path) {
		super("Path does not exist: \""+path.toAbsolutePath().toString()+"\"");
	}
}
