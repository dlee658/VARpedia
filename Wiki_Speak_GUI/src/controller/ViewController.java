package controller;

import java.io.File;
import java.util.Optional;

import helper.BashCommand;
import helper.CreationList;
import helper.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**page that display creation to user*/
public class ViewController {
	@FXML 
	private ListView<String> viewListView;
	private CreationList creationList;

	@FXML
	private Button playBtn;

	@FXML
	private Button deleteBtn;

	@FXML 
	private Button homeBtn;

	@FXML
	private HBox viewHB;

	@FXML
	private VBox helpBox;

	@FXML
	private Button helpBtn;

	private boolean helpOn = false;

	@FXML
	private void initialize() {
		creationList = new CreationList();
		viewListView.setItems(creationList.getCList()); 
		viewListView.setPlaceholder(new Label("No creations created"));
	}

	/**Return to main page*/
	@FXML
	private void handleHomeBtnAction(ActionEvent event) {
		SceneChanger.changeScene(null, "mainMenu.fxml", homeBtn);
	}

	/** Toggle on and off the help dialog when pressing the help button */
	@FXML
	private void handleHelpBtnAction(ActionEvent event) {
		helpOn  = !helpOn;
		if (helpOn) {
			viewHB.setDisable(true);
			helpBox.setVisible(true);
			helpBtn.setText("X");
		} else {
			viewHB.setDisable(false);
			helpBox.setVisible(false);
			helpBtn.setText("?");
		}
	}

	/** When user tries to play one of existing creation, go to video player page*/
	@FXML
	private void handlePlayBtnAction(ActionEvent event) {
		String fileName = viewListView.getSelectionModel().getSelectedItem();
		if (fileName == null) {
			return;
		}
		
		File file = new File("Creations" + File.separatorChar + fileName + ".mp4");
		VideoPlayer vpc = new VideoPlayer(file);
		
		// Switch to video player pane
		SceneChanger.changeScene(vpc, "videoPlayer.fxml", playBtn);
	}

	/** Allow user to delete existing creation*/
	@FXML
	private void handleDeleteBtnAction(ActionEvent event) {
		String fileName = viewListView.getSelectionModel().getSelectedItem();
		if (fileName == null) {
			return;
		}
		//ask user for confirmation
		ButtonType yesBtn = new ButtonType("yes");
		ButtonType noBtn = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE); 
		Alert deleteAlert = new Alert(AlertType.CONFIRMATION,"Are you sure you want to delete: " + fileName,yesBtn,noBtn);
		deleteAlert.setTitle("Confirm Deletion");
		deleteAlert.setHeaderText(null);

		Optional<ButtonType> btn = deleteAlert.showAndWait();
		if (btn.get() == yesBtn) {
			File file = new File("Creations" + File.separatorChar + fileName + ".mp4");
			file.delete();
			creationList.update();
			viewListView.setItems(creationList.getCList()); 
			updateCreationTermList(fileName);
			deleteAlert.close();
		} 
	}

	/** Update creation list*/
	private void updateCreationTermList(String fileName) {
		//Delete the creation term from txtfile
		String cmd = "sed -i '/"+fileName+",/d' ./creationTermList.txt";
		BashCommand.runCommand(cmd);

	}
}