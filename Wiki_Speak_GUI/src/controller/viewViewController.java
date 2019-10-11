package controller;

import java.io.IOException;

import application.CreationList;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

public class viewViewController {
	@FXML 
	private TableView<Creation> viewTable;
	private ObservableList<Creation> data = CreationList.getCList();

	@FXML 
	private TableColumn creationCol;
	
	@FXML 
	private TableColumn dateCol;
	
	@FXML 
	private TableColumn playCol;
	
	private Button playBtn;
	
	private Button deleteBtn;
	
	@FXML 
	private TableColumn deleteCol;
	
	@FXML 
	private Button homeBtn;

	private Pane rootLayout;
	
	@FXML
	private void initialize() {
		
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
	
	
	
	
	
	
	
	
}
