package org.sempmessaging.sempd.persistence.fs.io.lowlevel;

import org.sempmessaging.libsemp.arguments.Args;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandle {
	private final Path path;

	FileHandle(final Path path) {
		Args.notNull(path, "path");
		Args.is(Files.exists(path), "path", "must exist.");
		Args.is(Files.isRegularFile(path), "path", "must be a regular file.");

		this.path = path;
	}

	public Reader openReader() throws IOException {
		return new FileReader(path.toFile());
	}
}
