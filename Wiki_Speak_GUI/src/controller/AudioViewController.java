package controller;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class AudioViewController {
	@FXML 
	private Button homeBtn;

	@FXML 
	private Button previewBtn;

	@FXML 
	private Button saveBtn;

	@FXML 
	private Button nextBtn;

	@FXML 
	private TextArea resultArea;

	@FXML 
	private ChoiceBox<String> voiceCB;

	private Pane rootLayout;

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
	private void handleNextBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("creationPage.fxml"));
			rootLayout = loader.load();
			nextBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void initialize() {
		voiceCB.setValue("Male");
		voiceCB.getItems().addAll("Male", "NZ Guy", "Posh Lady");
	}


	@FXML
	private void handlePreviewBtnAction(ActionEvent event) {
		
	}
	
	@FXML
	private void handleSaveBtnAction(ActionEvent event) {
		
	}



}
