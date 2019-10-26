package controller;


import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.Main;
import helper.VideoCreation;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class CreateCreationController {
	private String _term;
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
	private Label instructLabel;
	@FXML
	private ProgressIndicator createIndicator;

	private Label msg;

	public CreateCreationController(String term, int numOfImages) {
		_term = term;
		_numOfImages = numOfImages;
		msg = new Label();
	}

	
	/**
	 * set home button to go back to main menu
	 */
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
				backBtn.getScene().setRoot(rootLayout);

			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}

	/**
	 * button for go back to previous page
	*/	
	@FXML
	private void handleBackBtnAction(ActionEvent event) {
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

	/**
	 * button to create creation and ask for the valid name for creation
	 */
	@FXML
	private void handleCreateBtnAction(ActionEvent event) {
		String name = txt.getText();
		if (isValidName(name) && !name.isBlank()) {
			vb.getChildren().clear();
			vb.getChildren().addAll(instructLabel,txt,createBtn,createIndicator);
			createIndicator.setVisible(true);

			ExecutorService createWorker = Executors.newSingleThreadExecutor(); 
			Task<File> task = new Task<File>() {
				@Override
				protected File call() throws Exception {
					VideoCreation vc = new VideoCreation();
					vc.createVideo(_term,_numOfImages,name);
					return null;	
				}
			};

			createWorker.submit(task);

			task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					createIndicator.setVisible(false);
					try {
						//create alert to ask to user
						ButtonType mainMenuBtn = new ButtonType("Return to Main Menu");
						ButtonType createAnotherBtn = new ButtonType("Create another");
						Alert deleteAlert = new Alert(AlertType.CONFIRMATION, null , mainMenuBtn,createAnotherBtn);
						deleteAlert.setTitle("Creation Complete");
						deleteAlert.setHeaderText("Creation '" +name+ "' created");
						Optional<ButtonType> btn = deleteAlert.showAndWait();
						//go to main menu
						if (btn.get() == mainMenuBtn) {
							FXMLLoader loader = new FXMLLoader();
							loader.setLocation(Main.class.getResource("mainMenu.fxml"));
							Pane rootLayout = loader.load();
							createBtn.getScene().setRoot(rootLayout);
						} 
						//go to search page 
						if(btn.get() == createAnotherBtn) {
							FXMLLoader loader = new FXMLLoader();
							loader.setLocation(Main.class.getResource("searchPage.fxml"));
							Pane rootLayout = loader.load();
							createBtn.getScene().setRoot(rootLayout);
						}

						updateCreationTermList(name,_term);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}

			});
			
		}else if (name.isBlank()){ 
			msg.setText("Please enter a name");
			vb.getChildren().clear();
			vb.getChildren().addAll(instructLabel,txt,createBtn,msg);


		} else {
			msg.setText("Creation '" +name+ "' already exists");
			vb.getChildren().clear();
			vb.getChildren().addAll(instructLabel,txt,createBtn,msg);


		}
	}

	/**
	 * update list of creation
	 */
	private void updateCreationTermList(String name, String term) {
		String command = "echo " + name + "," + term + " >> creationTermList.txt";
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
	/**
	 * enable enter button
	 */
	@FXML
	private void handleEnterKeyAction(KeyEvent key) {
		if (key.getCode().equals(KeyCode.ENTER)){
			createBtn.fire();
		}
	}
	/**
	 * check if there is file with same name already exists, and return false if there is a file with same name
	 */
	private boolean isValidName(String name) {
		File file = new File("Creations" + File.separatorChar + name + ".mp4");
		if(file.exists()) {
			return false;
		}
		return true;
	}






}
