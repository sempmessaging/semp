package org.sempmessaging.datastore.fs;

import java.nio.file.Path;

public interface FileSystemOperation {
	void performOperationIn(FileSystemDataStore.BasePath basePath);
}
