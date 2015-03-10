package org.sempmessaging.sempd.persistence.fs;

import net.davidtanzer.jevents.guice.EventComponentModule;
import org.sempmessaging.datastore.fs.operation.ReadEachFileOperationComponent;
import org.sempmessaging.libsemp.arguments.Args;

public class FileSystemPersistenceModule extends EventComponentModule {
	private final FileSystemPersistenceConfiguration fileSystemPersistenceConfiguration;

	public FileSystemPersistenceModule(final FileSystemPersistenceConfiguration fileSystemPersistenceConfiguration) {
		Args.notNull(fileSystemPersistenceConfiguration, "fileSystemPersistenceConfiguration");

		this.fileSystemPersistenceConfiguration = fileSystemPersistenceConfiguration;
	}
	@Override
	protected void configure() {
		bind(FileSystemPersistenceConfiguration.class).toInstance(fileSystemPersistenceConfiguration);

		bindEventComponent(ReadEachFileOperationComponent.class);
	}
}
