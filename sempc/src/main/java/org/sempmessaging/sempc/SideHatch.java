package org.sempmessaging.sempc;

import com.google.inject.Injector;
import org.sempmessaging.sempc.ui.MainHtmlPage;

public interface SideHatch {
	void start(MainHtmlPage mainHtmlPage);
	void shutdown();
}
