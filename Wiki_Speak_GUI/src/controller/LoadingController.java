package controller;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import helper.DownloadImageTask;
import helper.SceneChanger;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

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
				SceneChanger.changeScene(null, "mainMenu.fxml", homeBtn);
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
		RetrieveImage controller = new RetrieveImage(_term);
		ExecutorService worker = Executors.newSingleThreadExecutor(); 
		
		DownloadImageTask dlTask = new DownloadImageTask(_term);
		worker.submit(dlTask);
		
		dlTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				SceneChanger.changeScene(controller, "retrieveImage.fxml", homeBtn);
			}
		});
	}
}
