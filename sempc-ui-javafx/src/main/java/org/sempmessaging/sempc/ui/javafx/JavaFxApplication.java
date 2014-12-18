package org.sempmessaging.sempc.ui.javafx;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.sempmessaging.libsemp.arguments.Args;

public class JavaFxApplication extends Application {
	@Override
	public void start(final Stage primaryStage) throws Exception {
		SplitPane splitPane = new SplitPane();
		splitPane.setOrientation(Orientation.HORIZONTAL);
		splitPane.setDividerPositions(0.2);

		WebView conversationsView = new WebView();

		BorderPane mainConversationView = new BorderPane();

		WebView conversationDetailsView = new WebView();
		mainConversationView.setCenter(conversationDetailsView);

		AnchorPane conversationButtons = new AnchorPane();
		conversationButtons.setMinHeight(25);
		conversationButtons.setStyle("-fx-border-color: black transparent transparent transparent;" +
				"-fx-background-color: #ccc;");
		mainConversationView.setBottom(conversationButtons);

		splitPane.getItems().addAll(conversationsView, mainConversationView);

		primaryStage.setScene(new Scene(splitPane));
		primaryStage.setTitle("SEMP Client");
		primaryStage.show();
	}

	public void startApplication(final String[] commandLineArgs) {
		Args.notNull(commandLineArgs, "commandLineArgs");
		launch(commandLineArgs);
	}
}
