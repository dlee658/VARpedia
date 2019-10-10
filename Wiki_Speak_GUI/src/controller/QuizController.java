package controller;
import java.awt.Label;
import java.io.IOException;

import com.sun.prism.paint.Color;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class QuizController {
	@FXML 
	private Button exitBtn;
	
	@FXML
	private Text questionN;
	
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
           Pane rootLayout = loader.load();
           exitBtn.getScene().setRoot(rootLayout);
           questionNumber = 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void playBtn(ActionEvent event) {
		if (playBtn.getText().equals("Play")) {
			playBtn.setText("Pause");
		}
		else {
			playBtn.setText("Play");
		}
	}
	

	@FXML
	private void opt1Press(ActionEvent event) {

			questionNumber = questionNumber +1;
			// Load root layout from fxml file.
			 questionN.setText("Question " + Integer.toString(questionNumber));
			 
			 //if correct
			 opt1.setStyle("-fx-background-color: #00ff00");
			 //if wrong
			 opt1.setStyle("-fx-background-color: #ff0000");
			 
			 


	}
	
	@FXML
	private void opt2Press(ActionEvent event) {
		questionNumber = questionNumber +1;
		// Load root layout from fxml file.

		questionN.setText("Question " + Integer.toString(questionNumber));
		 //if correct
		 opt2.setStyle("-fx-background-color: #00ff00");
		 //if wrong
		 opt2.setStyle("-fx-background-color: #ff0000");
	}
	
	@FXML
	private void opt3Press(ActionEvent event) {

			questionNumber = questionNumber +1;
			// Load root layout from fxml file.
			 questionN.setText("Question " + Integer.toString(questionNumber));

			 //if correct
			 opt3.setStyle("-fx-background-color: #00ff00");
			 //if wrong
			 opt3.setStyle("-fx-background-color: #ff0000");
			 
			 

	}
	
	
	@FXML
	private void opt4Press(ActionEvent event) {

			questionNumber = questionNumber +1;
			// Load root layout from fxml file.
			 questionN.setText("Question " + Integer.toString(questionNumber));
			 //if correct
			 opt4.setStyle("-fx-background-color: #00ff00");
			 //if wrong
			 opt4.setStyle("-fx-background-color: #ff0000");
			
	}
	
}
