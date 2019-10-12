package controller;

import java.io.File;
import java.io.IOException;

import application.CreationList;
import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

public class viewViewController {
	@FXML 
	private ListView<String> viewListView;
	private CreationList creationList;

	@FXML
	private Button playBtn;
	@FXML
	private Button deleteBtn;
	
	@FXML 
	private Button homeBtn;

	private Pane rootLayout;
	
	@FXML
	private void initialize() {
		creationList = new CreationList();
		viewListView.setItems(creationList.getCList()); 
		viewListView.setPlaceholder(new Label("No creations created"));
	}
	
	@FXML
	private void handleHomeBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("searchPage.fxml"));
			rootLayout = loader.load();
			homeBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
	
	@FXML
	private void handleDeleteBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("searchPage.fxml"));
			rootLayout = loader.load();
			homeBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}