package controller;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class SearchPageController {
	@FXML 
	private Button homeBtn;
	
	@FXML 
	private Button searchBtn;
	
	@FXML 
	private Button nextBtn;
	
	@FXML 
	private TextField searchField;
	
	@FXML 
	private TextArea resultArea;
	
	@FXML 
	private ProgressIndicator searchIndicator;

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
	private void handleSearchBtnAction(ActionEvent event) {
		try {
			
			//enable progress indicator
			//change search to cancel
			//do searching process
			//enable next btn
			
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleNextBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
		   FXMLLoader loader = new FXMLLoader();
           loader.setLocation(Main.class.getResource("audioView.fxml"));
           rootLayout = loader.load();
           nextBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
