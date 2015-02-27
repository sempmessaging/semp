package org.sempmessaging.datastore.fs.lowlevel;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileSystemTester {
	private final Path basePath;

	public FileSystemTester() throws IOException {
		basePath = FileSystems.getDefault().getPath("fileSystemTestDirectory");
		if(Files.exists(basePath)) {
			deleteBasePath();
		}
		Files.createDirectory(basePath);
	}

	public Path basePath() {
		return basePath;
	}

	public void teardown() throws IOException {
		deleteBasePath();
	}

	public void createFile(final String name) throws IOException {
		createFile(name, "");
	}

	public void createFile(final String name, final String content) throws IOException {
		FileWriter writer = new FileWriter(basePath.resolve(name).toFile());
		try {
			writer.write(content);
		} finally {
			writer.close();
		}
	}

	public void createDirectory(final String name) throws IOException {
		Files.createDirectory(basePath.resolve(name));
	}

	private void deleteBasePath() throws IOException {
		Files.walkFileTree(basePath, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}

		});
	}

}
