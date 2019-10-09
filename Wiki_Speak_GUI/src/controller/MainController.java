package controller;

import java.awt.Label;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class MainController {
	@FXML 
	private Button exitBtn;
	
	@FXML
	private Label questionN;
	
	@FXML 
	private Button opt1;
	
	@FXML 
	private Button opt2;
	
	@FXML 
	private Button opt3;
	
	@FXML 
	private Button opt4;
	
	@FXML
	private Button playBtn;

	private int questionNumber = 1;
	
	@FXML
	private void handleExitBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
		   FXMLLoader loader = new FXMLLoader();
           loader.setLocation(Main.class.getResource("mainMenu.fxml"));
           rootLayout = loader.load();
           salesBtn.getScene().setRoot(rootLayout);
           questionNumer = 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
