package application;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
//import javafx.scene.control.TabPane;
//import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.Pane;

/**
 * Main method of application
 * @author student
 *
 */
public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Wiki-Speak Authoring Tool");
		primaryStage.setResizable(false);

		setUpDirectories();

		try {
		 FXMLLoader loader = new FXMLLoader();
         loader.setLocation(Main.class.getResource("mainMenu.fxml"));
         Pane rootLayout;
		rootLayout = loader.load();
		primaryStage.setScene(new Scene(rootLayout));
		primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * Sets up the directories for creation, audio and video files
	 */
	private void setUpDirectories() {
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
