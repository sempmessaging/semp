package org.sempmessaging.sempd.persistence.fs.io;

import net.davidtanzer.jevents.Parallel;
import net.davidtanzer.jevents.ParallelPolicy;

import java.io.Reader;

public interface ReadingFileEvent {
	@Parallel(ParallelPolicy.NEVER)
	void readingFile(Reader reader);
}
