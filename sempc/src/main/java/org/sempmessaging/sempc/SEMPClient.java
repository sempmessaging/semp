package org.sempmessaging.sempc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.sempmessaging.sempc.core.SempcCoreModule;
import org.sempmessaging.sempc.core.account.Accounts;
import org.sempmessaging.sempc.ui.SempcUiModule;
import org.sempmessaging.sempc.ui.javafx.JavaFxUiModule;
import org.sempmessaging.sempc.ui.javafx.MainView;
import org.sempmessaging.sempc.ui.javafx.urls.SEMPUrlStreamHandlerFactory;

import java.net.URL;

public class SEMPClient extends Application {
	private static SideHatch sideHatch;
	private final Injector injector;

	public SEMPClient() {
		injector = Guice.createInjector(new JavaFxUiModule(), new SempcCoreModule(), new SempcUiModule(), new SempClientModule());
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		SEMPUrlStreamHandlerFactory urlStreamHandlerFactory = injector.getInstance(SEMPUrlStreamHandlerFactory.class);
		URL.setURLStreamHandlerFactory(urlStreamHandlerFactory);

		MainView mainView = injector.getInstance(MainView.class);

		primaryStage.setScene(new Scene(mainView));
		primaryStage.setTitle("SEMP Client");
		primaryStage.show();
		primaryStage.setOnCloseRequest((windowEvent) -> {
			if(sideHatch != null) {
				sideHatch.shutdown();
			}
		});

		Accounts accounts = injector.getInstance(Accounts.class);
		accounts.connect();

		if(sideHatch != null) {
			sideHatch.start(mainView.getHtmlPage());
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	static void launchWithSideHatch(final SideHatch sideHatch, final String[] args) {
		SEMPClient.sideHatch = sideHatch;
		launch(args);
	}
}
