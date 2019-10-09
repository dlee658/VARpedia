package controller;
import java.awt.Label;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class QuizController {
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
	
	@FXML
	private void playBtn(ActionEvent event) {
		try {
			if (playBtn.getText().equals("Play")) {
				playBtn.setText("Pause");
			}
			else {
				playBtn.setText("Play");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	@FXML
	private void opt1Press(ActionEvent event) {
		try {
			questionNumber = questionNumber +1;
			// Load root layout from fxml file.
			 questionN.setText("Question " + Integer.toString(questionNumber));
			 
			 
  
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void opt2Press(ActionEvent event) {
		try {
			questionNumber = questionNumber +1;
			// Load root layout from fxml file.
		 questionN.setText("Question " + Integer.toString(questionNumber));
  
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void opt3Press(ActionEvent event) {
		try {
			questionNumber = questionNumber +1;
			// Load root layout from fxml file.
			 questionN.setText("Question " + Integer.toString(questionNumber));
  
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@FXML
	private void opt4Press(ActionEvent event) {
		try {
			questionNumber = questionNumber +1;
			// Load root layout from fxml file.
			 questionN.setText("Question " + Integer.toString(questionNumber));
  
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
