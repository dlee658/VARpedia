package application;

import java.io.File;
import java.io.IOException;

import helper.BashCommand;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;

/**
 * Main method of application
 * Setting main menu scene using fxml
 */
public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("VARpedia");
		primaryStage.setResizable(false);

		setUpDirectories();

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("mainMenu.fxml"));
			Scene scene = new Scene(loader.load()); 
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//delete every unnecessary files
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				String cmd = "rm Audio/*.txt; rm  Audio/*.wav; rm *.jpg; rm -r Temp; rm Quiz/QuizList.txt";
				BashCommand.runCommand(cmd);
			}
		});
	}

	/**
	 * Sets up the directories for creation, audio and video files
	 */
	private void setUpDirectories() {
		File creation = new File("Creations");
		File audio = new File("Audio");
		File quiz = new File("Quiz");
		File temp = new File("Temp");

		if (!creation.isDirectory()) {
			creation.mkdir();
		}
		if (!audio.isDirectory()) {
			audio.mkdir();
		}
		if (!quiz.isDirectory()) {
			quiz.mkdir();
		}
		if (!temp.isDirectory()) {
			temp.mkdir();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
