package org.sempmessaging.sempc.ui;

import net.davidtanzer.html.Node;

public interface ComponentChangedListener {
	void htmlComponentChanged(String id, Node[] newContent);
}
