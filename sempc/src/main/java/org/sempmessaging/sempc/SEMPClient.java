package org.sempmessaging.sempc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.sempmessaging.sempc.ui.javafx.MainWindow;

public class SEMPClient {
	private final Injector injector;

	public SEMPClient() {
		injector = Guice.createInjector();
	}

	public void start() {
		MainWindow mainWindow = injector.getInstance(MainWindow.class);
		mainWindow.show();
	}

	public static void main(String[] args) {
		SEMPClient client = new SEMPClient();
		client.start();
	}

}
