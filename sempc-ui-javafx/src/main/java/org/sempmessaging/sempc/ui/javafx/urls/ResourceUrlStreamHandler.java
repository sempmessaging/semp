package org.sempmessaging.sempc.ui.javafx.urls;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class ResourceUrlStreamHandler extends URLStreamHandler {
	@Override
	protected URLConnection openConnection(final URL url) throws IOException {
		return new ResourceUrlConnection(url);
	}
}
