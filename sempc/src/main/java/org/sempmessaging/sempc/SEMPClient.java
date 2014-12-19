package org.sempmessaging.sempc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.sempmessaging.sempc.ui.javafx.MainView;

public class SEMPClient extends Application {
	private final Injector injector;

	public SEMPClient() {
		injector = Guice.createInjector();
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		MainView mainView = injector.getInstance(MainView.class);

		primaryStage.setScene(new Scene(mainView));
		primaryStage.setTitle("SEMP Client");
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
