package controller;
import java.awt.Label;
import java.io.IOException;
import java.util.Random;

import com.sun.prism.paint.Color;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

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
	
	@FXML
	private Label scoreLabel;

	private int questionNumber = 1;
	private int score =0;
	protected String term = "apple";

	
	protected int answerNumber;
	
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
	
	public boolean isAnswer(int n) {
		if(answerNumber == n) {
		return true;}	
		else {
		return false;}
		
	}
	
	public void setAnswer() {
        Random rand = new Random();
        int randInt = rand.nextInt(4) + 1;
		answerNumber = randInt;
        if (randInt == 1) {
        	opt1.setText(term);
    	
        }
        if (randInt == 2) {
        	opt2.setText(term);
    	
        }
        if (randInt == 3) {
        	opt3.setText(term);
    	
        }
        if (randInt == 4) {
        	opt4.setText(term);
    	
        }
        
        
        
	}
	
	public void finished() {
		try {
			
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Game finished");
			alert.setHeaderText(null);
			alert.setContentText("Your score is " + Integer.toString(score));
			alert.show();
			
			// Load root layout from fxml file.
		   FXMLLoader loader = new FXMLLoader();
           loader.setLocation(Main.class.getResource("mainMenu.fxml"));
           Pane rootLayout = loader.load();
           exitBtn.getScene().setRoot(rootLayout);
           questionNumber = 1;
           score = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	

	@FXML
	private void opt1Press(ActionEvent event) {
		
		if(questionN.getText().equals("Question One")) {
			questionN.setText("Question 1");
			setAnswer();
		}
		else {
		
		
		

			questionNumber = questionNumber +1;
			
			if (questionNumber == 11) {
				finished();
			}
			
			// Load root layout from fxml file.
			 questionN.setText("Question " + Integer.toString(questionNumber));
			 
			if (isAnswer(1)) {
			 opt1.setStyle("-fx-background-color: #00ff00");
			 score = score +1;
			 scoreLabel.setText(Integer.toString(score));
			}
			else {
			 //if wrong
			 opt1.setStyle("-fx-background-color: #ff0000");}
			 
			setAnswer();
			 
		}

	}
	
	@FXML
	private void opt2Press(ActionEvent event) {
		
		if(questionN.getText().equals("Question One")) {
			questionN.setText("Question 1");
			setAnswer();
		}
		else {
		questionNumber = questionNumber +1;
		
		if (questionNumber == 11) {
			finished();
		}
		
		// Load root layout from fxml file.
		 questionN.setText("Question " + Integer.toString(questionNumber));
		 
		if (isAnswer(2)) {
		 opt2.setStyle("-fx-background-color: #00ff00");
		 score = score +1;
		 scoreLabel.setText(Integer.toString(score));
		}
		else {
		 //if wrong
		 opt2.setStyle("-fx-background-color: #ff0000");}
		 
		setAnswer();}
	}
	
	@FXML
	private void opt3Press(ActionEvent event) {

		if(questionN.getText().equals("Question One")) {
			questionN.setText("Question 1");
			setAnswer();
		}
		else {
		questionNumber = questionNumber +1;
		
		if (questionNumber == 11) {
			finished();
		}
		
		// Load root layout from fxml file.
		 questionN.setText("Question " + Integer.toString(questionNumber));
		 
		if (isAnswer(3)) {
		 opt3.setStyle("-fx-background-color: #00ff00");
		 score = score +1;
		 scoreLabel.setText(Integer.toString(score));
		}
		else {
		 //if wrong
		 opt3.setStyle("-fx-background-color: #ff0000");}
		 
		setAnswer();
			 
		}

	}
	
	
	@FXML
	private void opt4Press(ActionEvent event) {

		if(questionN.getText().equals("Question One")) {
			questionN.setText("Question 1");
			setAnswer();
		}
		else {
		questionNumber = questionNumber +1;
		
		if (questionNumber == 11) {
			finished();
		}
		
		// Load root layout from fxml file.
		 questionN.setText("Question " + Integer.toString(questionNumber));
		 
		if (isAnswer(4)) {
		 opt4.setStyle("-fx-background-color: #00ff00");
		 score = score +1;
		 scoreLabel.setText(Integer.toString(score));
		}
		else {
		 //if wrong
		 opt4.setStyle("-fx-background-color: #ff0000");}
		 
		setAnswer();
		}
			
	}
	
}
