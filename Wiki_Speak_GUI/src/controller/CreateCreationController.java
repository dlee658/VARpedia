package controller;


import java.io.File;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import helper.BashCommand;
import helper.SceneChanger;
import helper.VideoCreation;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;


public class CreateCreationController {
	private String _term;
	private int _numOfImages;
	@FXML
	private Button homeBtn;
	@FXML
	private VBox createVB;
	@FXML
	private Button helpBtn;
	@FXML
	private VBox helpBox;
	@FXML
	private Button createBtn;
	@FXML
	private TextField nameField;
	@FXML
	private Label instructLabel;
	@FXML
	private ProgressIndicator createIndicator;

	private Label msg;

	private boolean helpOn = false;

	public CreateCreationController(String term, int numOfImages) {
		_term = term;
		_numOfImages = numOfImages;
		msg = new Label();
	}

	@FXML
	public void initialize() {
		setRecommendedName();

	}

	private void setRecommendedName() {
		String name = _term;
		int i = 0;
		while(!isValidName(name)) {
			i++;
			name = _term + "_" + i;
		}
		nameField.setText(name);

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
			SceneChanger.changeScene(null, "mainMenu.fxml", homeBtn);
		} 
	}

	/**
	 * Button to get the help for this page
	 */	
	@FXML
	private void handleHelpBtnAction(ActionEvent event) {
		helpOn  = !helpOn;
		if (helpOn ) {
			createVB.setDisable(true);
			helpBox.setVisible(true);
			helpBtn.setText("X");
		} else {
			createVB.setDisable(false);
			helpBox.setVisible(false);
			helpBtn.setText("?");
		}

	}

	/**
	 * button to create creation and ask for the valid name for creation
	 */
	@FXML
	private void handleCreateBtnAction(ActionEvent event) {
		String name = nameField.getText();
		if (isValidName(name) && !name.isBlank()) {
			msg.setText("");
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
					//create alert to ask to user
					ButtonType mainMenuBtn = new ButtonType("Return to Main Menu");
					ButtonType createAnotherBtn = new ButtonType("Create another");
					Alert deleteAlert = new Alert(AlertType.CONFIRMATION, null , mainMenuBtn,createAnotherBtn);
					deleteAlert.setTitle("Creation Complete");
					deleteAlert.setHeaderText("Creation '" +name+ "' created");
					Optional<ButtonType> btn = deleteAlert.showAndWait();
					//go to main menu
					if (btn.get() == mainMenuBtn) {
						SceneChanger.changeScene(null, "mainMenu.fxml", createBtn);
					} 
					//go to search page 
					if(btn.get() == createAnotherBtn) {
						SceneChanger.changeScene(null, "searchPage.fxml", createBtn);
					}
					updateCreationTermList(name,_term);
				}

			});

		} else if (name.isBlank()){ 
			msg.setText("Please enter a name");

		} else {
			msg.setText("Creation '" +name+ "' already exists");
		}
	}

	/**
	 * update list of creation
	 */
	private void updateCreationTermList(String name, String term) {
		String cmd = "echo " + name + "," + term + " >> creationTermList.txt";
		BashCommand.runCommand(cmd);	
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
