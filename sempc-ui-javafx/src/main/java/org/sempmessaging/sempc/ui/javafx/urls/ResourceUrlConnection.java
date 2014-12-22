package org.sempmessaging.sempc.ui.javafx.urls;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ResourceUrlConnection extends URLConnection {
	protected ResourceUrlConnection(final URL url) {
		super(url);
	}

	@Override
	public void connect() throws IOException {
		if(connected) {
			throw new IllegalStateException("Cannot call \"connect()\" again: Already connected.");
		}

		connected = true;
	}

	@Override
	public boolean getDoInput() {
		return true;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		String resourceName = url.getPath();
		return getClass().getResourceAsStream(resourceName);
	}
}
