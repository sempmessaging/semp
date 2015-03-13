package org.sempmessaging.sempc.persistence.fs.account.config;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.sempmessaging.datastore.fs.FileSystemAccess;
import org.sempmessaging.datastore.fs.operation.ReadFileOperation;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.core.account.config.AccountConfiguration;
import org.sempmessaging.sempc.core.account.config.AccountConfigurationRepository;

import java.util.List;

public class FileSystemAccountConfigurationRepository implements AccountConfigurationRepository {
	private final FileSystemAccess fileSystemAccess;
	private final Provider<ReadFileOperation> readFileOperationProvider;

	@Inject
	public FileSystemAccountConfigurationRepository(final FileSystemAccess fileSystemAccess, final Provider<ReadFileOperation> readFileOperationProvider) {
		Args.notNull(fileSystemAccess, "fileSystemAccess");
		Args.notNull(readFileOperationProvider, "readFileOperationProvider");

		this.fileSystemAccess = fileSystemAccess;
		this.readFileOperationProvider = readFileOperationProvider;
	}

	@Override
	public List<AccountConfiguration> listAll() {
		return null;
	}
}
