package controller;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.Main;
import helper.DownloadImageTask;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;

/**
 * this page is the loading page for user to wait until image is created
 * */
public class LoadingController {
	String _term;
	@FXML 
	private Button homeBtn;
	/**
	 * return to main page button
	 * */
	@FXML
	private void handleHomeBtnAction(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Home");
		alert.setHeaderText("Are you sure you want to leave?");
		alert.setContentText("Current creation will not be saved.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			try {
				// Load root layout from fxml file.
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("mainMenu.fxml"));
				Pane rootLayout = loader.load();
				homeBtn.getScene().setRoot(rootLayout);

			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}
	
	
	public LoadingController(String term){		
		_term = term;
		downloadImages();
	}
	
	/**
	 * Retrieve image with given search term, and when all image been download, go to retrieve image page
	 * it is implemented using worker so GUI not froze
	 * */
	public void downloadImages(){
		try {
			RetrieveImage controller = new RetrieveImage(_term);
			ExecutorService worker = Executors.newSingleThreadExecutor(); 
			
			DownloadImageTask dlTask = new DownloadImageTask(_term);
			worker.submit(dlTask);
			
			dlTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					try {
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(Main.class.getResource("retrieveImage.fxml"));
						loader.setController(controller);
						homeBtn.getScene().setRoot(loader.load());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
