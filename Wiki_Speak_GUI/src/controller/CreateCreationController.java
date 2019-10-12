package controller;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.Main;
import application.VideoCreation;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class CreateCreationController {


	private String term;
	private int _numOfImages;
	
	
	
	@FXML
	private Button homeBtn;
	@FXML
	private VBox vb;
	@FXML
	private Button backBtn;
	@FXML
	private Button createBtn;
	@FXML
	private TextField txt;
	@FXML
	private ProgressIndicator createIndicator;
	
	private Label msg;
	
	public CreateCreationController(int numOfImages) {
		_numOfImages = numOfImages;
		msg = new Label();
	}

	@FXML
	public void initialize() {

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("term.txt"));
			term = reader.readLine();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	@FXML
	private void handleHomeBtnAction(ActionEvent event) {
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
	
	@FXML
	private void handleBackBtnAction(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Back");
		alert.setHeaderText("Are you sure you want to leave?");
		alert.setContentText("Current creation will not be saved.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			try {
				// Load root layout from fxml file.
			   FXMLLoader loader = new FXMLLoader();
	           loader.setLocation(Main.class.getResource("retrieveImage.fxml"));
	           Pane rootLayout = loader.load();
	           backBtn.getScene().setRoot(rootLayout);

			} catch (IOException e) {
				e.printStackTrace();
			}
		} 

	}
	
	@FXML
	private void handleCreateBtnAction(ActionEvent event) {
		String name = txt.getText();
		if (isValidName(name) && !name.isBlank()) {
			vb.getChildren().remove(msg);
			vb.getChildren().add(createIndicator);
			createIndicator.setVisible(true);
			String audio = "\"Audio" + File.separatorChar + term +".wav\"";

			ExecutorService createWorker = Executors.newSingleThreadExecutor(); 
			Task<File> task = new Task<File>() {
				@Override
				protected File call() throws Exception {
					try { 
						String cmd = "for f in Audio/*.wav; do echo \"file '$f'\" >> mylist.txt ; done ; ffmpeg -safe 0 -y -f concat -i mylist.txt -c copy " + audio + "; rm mylist.txt";

						ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
						Process audioProcess = pb.start();
						audioProcess.waitFor();

						VideoCreation vc = new VideoCreation();
						vc.createVideo(term,_numOfImages,name);

					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return null;	
				}
			};

			createWorker.submit(task);
			task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					createIndicator.setVisible(false);
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Creation complete");
					alert.setHeaderText(null);
					alert.setContentText("Creation '" +name+ "' created");
					alert.show();
				}	
			});
		}else if (name.isBlank()){ 
			vb.getChildren().remove(createIndicator);
			vb.getChildren().add(msg);
			msg.setText("Please enter a name");
			
		} else {
			vb.getChildren().remove(createIndicator);
			vb.getChildren().add(msg);
			msg.setText("Creation '" +name+ "' already exists");
			
		}
	}
	
	
	private boolean isValidName(String name) {
		File file = new File("Creations" + File.separatorChar + name + ".mp4");
		if(file.exists()) {
			return false;
		}
		return true;
	}
	
	
	
	
	
	
}
