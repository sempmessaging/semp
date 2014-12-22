package org.sempmessaging.sempc.ui.javafx.urls;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.sempmessaging.libsemp.arguments.Args;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class SEMPUrlStreamHandlerFactory implements URLStreamHandlerFactory {
	private final Provider<ResourceUrlStreamHandler> resourceUrlStreamHandlerProvider;

	@Inject
	SEMPUrlStreamHandlerFactory(final Provider<ResourceUrlStreamHandler> resourceUrlStreamHandlerProvider) {
		Args.notNull(resourceUrlStreamHandlerProvider, "resourceUrlStreamHandlerProvider");
		this.resourceUrlStreamHandlerProvider = resourceUrlStreamHandlerProvider;
	}

	@Override
	public URLStreamHandler createURLStreamHandler(final String protocol) {
		if("res".equals(protocol)) {
			return resourceUrlStreamHandlerProvider.get();
		}
		return null;
	}
}
