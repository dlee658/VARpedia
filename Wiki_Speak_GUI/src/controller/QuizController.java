package controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;


import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;;

public class QuizController {
	@FXML 
	private Button exitBtn;
	
	@FXML
	private Label questionN;
	
	@FXML
	private MediaView mediaPlayer;

	@FXML
	private Button enterBtn;
	
	@FXML
	private TextField answerField;
	
	@FXML
	private Label scoreLabel;
	//
	private String answer = "apple";
	//
	private int questionNumber = 1;
	private int score =0;

	
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
	//
	@FXML
	private void handlePlayBtnAction(ActionEvent event) {

	}
	//
	@FXML
	private void handleEnterBtnAction(ActionEvent event) {
		String input = answerField.getText();
		if(isCorrect(input)) {
			score = score +1;
			scoreLabel.setText(Integer.toString(score));
		}
		questionNumber = questionNumber +1;
		 questionN.setText("Question " + Integer.toString(questionNumber));
	}
	
	
	
	
	
	
	@FXML
	public void initialize() {
		//generateVideo();
		
		
	}

	public boolean isCorrect(String term) {
		if(term.equals(answer)){
			return true;
		}
		else {
			return false;
		}
	}

	public void finished() {
		try {
			
			
/*			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Game finished");
			alert.setHeaderText("Your score is " + Integer.toString(score));
			alert.setContentText("Press ok to go back to main menu, or try again");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				   FXMLLoader loader = new FXMLLoader();
		           loader.setLocation(Main.class.getResource("mainMenu.fxml"));
		           Pane rootLayout = loader.load();
		           exitBtn.getScene().setRoot(rootLayout);
		           questionNumber = 1;
		           score = 0;
			} else {	
				   FXMLLoader loader = new FXMLLoader();
		           loader.setLocation(Main.class.getResource("quizView.fxml"));
		           Pane rootLayout = loader.load();
		           exitBtn.getScene().setRoot(rootLayout);
		           questionNumber = 1;
		           score = 0;		
			}
			*/
			
			ButtonType yesBtn = new ButtonType("Main menu");
			ButtonType noBtn = new ButtonType("Play again");
			Alert deleteAlert = new Alert(AlertType.CONFIRMATION,"Press button to play again or return " , yesBtn,noBtn);
			deleteAlert.setTitle("Game finished");
			deleteAlert.setHeaderText("Your score is " + Integer.toString(score));

			Optional<ButtonType> btn = deleteAlert.showAndWait();

			if (btn.get() == yesBtn) {
				   FXMLLoader loader = new FXMLLoader();
		           loader.setLocation(Main.class.getResource("mainMenu.fxml"));
		           Pane rootLayout = loader.load();
		           exitBtn.getScene().setRoot(rootLayout);
		           questionNumber = 1;
		           score = 0;
			} 
			if(btn.get() == noBtn) {
				   FXMLLoader loader = new FXMLLoader();
		           loader.setLocation(Main.class.getResource("quizView.fxml"));
		           Pane rootLayout = loader.load();
		           exitBtn.getScene().setRoot(rootLayout);
		           questionNumber = 1;
		           score = 0;	
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
}
