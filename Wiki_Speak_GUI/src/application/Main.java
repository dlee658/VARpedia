package application;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
		primaryStage.setTitle("Varpedia");
		primaryStage.setResizable(false);

		setUpDirectories();

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("mainMenu.fxml"));
			Pane rootLayout;
			rootLayout = loader.load();
			Scene scene = new Scene(rootLayout); 
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				String command = "rm Audio/*.txt; rm  Audio/*.wav; rm *.jpg";
				ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);			
				try {
					Process searchProcess = pb.start(); 
					searchProcess.waitFor();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		});


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
