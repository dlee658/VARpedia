package controller;

import java.awt.Label;
import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class MainController {
	@FXML 
	private Button createBtn;
	
	@FXML 
	private Button viewBtn;
	
	@FXML 
	private Button quizBtn;
	
	@FXML 
	private Button helpBtn;

	private Pane rootLayout;

	
	@FXML
	private void handleQuizBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
		   FXMLLoader loader = new FXMLLoader();
           loader.setLocation(Main.class.getResource("quizView.fxml"));
           rootLayout = loader.load();
           quizBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
