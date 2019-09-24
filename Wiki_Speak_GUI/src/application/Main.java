package application;

import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Wiki-Speak Authoring Tool");
		primaryStage.setMinHeight(500);
		primaryStage.setMinWidth(600);

		setUpDirectories();
		Home home = new Home();
		View view = new View();
		Create create = new Create(view);
		
		//Set up the tab panes
		TabPane tabPane = new TabPane(home.getTab(),create.getTab(),view.getTab());
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		primaryStage.setScene(new Scene(tabPane,600,500));
		primaryStage.show();
		
	}

	private void setUpDirectories() {
		//create creation, audio and video files

		File creation = new File("Creations");
		File audio = new File("Audio");
		File video = new File("Video");

		if (!creation.isDirectory()) {
			creation.mkdir();
		}
		if (!audio.isDirectory()) {
			audio.mkdir();
		}
		if (!video.isDirectory()) {
			video.mkdir();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
