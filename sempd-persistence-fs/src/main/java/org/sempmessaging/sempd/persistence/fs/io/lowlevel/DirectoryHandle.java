package org.sempmessaging.sempd.persistence.fs.io.lowlevel;

import org.sempmessaging.libsemp.arguments.Args;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryHandle {
	private final Path path;

	DirectoryHandle(final Path path) {
		Args.notNull(path, "path");
		Args.is(Files.exists(path), "path", "must exist.");
		Args.is(Files.isDirectory(path), "path", "must be a directory.");

		this.path = path;
	}

	public void forEachFile(final FileHandleProcessor processor) throws IOException {
		Args.notNull(processor, "processor");

		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
		for(Path subPath : directoryStream) {
			if(Files.isRegularFile(subPath)) {
				processor.processFile(new FileHandle(subPath));
			}
		}

		directoryStream.close();
	}
}
