package controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import application.CreationList;
import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

	private boolean helpOn = false;
	
	@FXML
	private HBox viewHB;

	@FXML
	private VBox helpBox;
	
	private Pane rootLayout;
	
	@FXML
	private Button helpBtn;
	
	@FXML
	private void initialize() {
		creationList = new CreationList();
		viewListView.setItems(creationList.getCList()); 
		viewListView.setPlaceholder(new Label("No creations created"));
	}
	
	/**go to main page*/
	@FXML
	private void handleHomeBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("mainMenu.fxml"));
			rootLayout = loader.load();
			homeBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleHelpBtnAction(ActionEvent event) {
		helpOn  = !helpOn;
		if (helpOn  ) {
			viewHB.setDisable(true);
			helpBox.setVisible(true);
			helpBtn.setText("X");
		} else {
			viewHB.setDisable(false);
			helpBox.setVisible(false);
			helpBtn.setText("?");
		}

	}
	
	/**when user tries to play one of existing creation, go to video player page*/
	@FXML
	private void handlePlayBtnAction(ActionEvent event) {
		try {
			String fileName = viewListView.getSelectionModel().getSelectedItem();
			if (fileName == null) {
				return;
			}
			File file = new File("Creations" + File.separatorChar + fileName + ".mp4");
			VideoPlayer vpc = new VideoPlayer(file);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("videoPlayer.fxml"));
			loader.setController(vpc);			
			playBtn.getScene().setRoot(loader.load());
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**allow user to delete existing creation*/
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

	/**update creation list*/
	private void updateCreationTermList(String fileName) {
		String command = "sed -i '/"+fileName+",/d' ./creationTermList.txt";
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
	
	
	
	
}