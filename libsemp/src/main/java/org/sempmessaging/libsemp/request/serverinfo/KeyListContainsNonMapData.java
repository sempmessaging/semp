package org.sempmessaging.libsemp.request.serverinfo;

import org.sempmessaging.libsemp.request.RequestError;

public class KeyListContainsNonMapData extends RequestError {
	public KeyListContainsNonMapData(final Object o) {
		super("Server sent a key list which contains non-map data (\""+o.getClass()+"\").");
	}
}
