package org.sempmessaging.sempc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.javafx.JavaFxApplication;

public class SEMPClient {
	private final Injector injector;

	public SEMPClient() {
		injector = Guice.createInjector();
	}

	public void start(final String[] commandLineArgs) {
		Args.notNull(commandLineArgs, "commandLineArgs");

		JavaFxApplication application = injector.getInstance(JavaFxApplication.class);
		application.startApplication(commandLineArgs);
	}

	public static void main(String[] args) {
		SEMPClient client = new SEMPClient();
		client.start(args);
	}

}
