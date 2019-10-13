package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
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
	private int questionNumber;
	private int score = 0;

	
	@FXML
	private void handleExitBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
		   FXMLLoader loader = new FXMLLoader();
           loader.setLocation(Main.class.getResource("mainMenu.fxml"));
           Pane rootLayout = loader.load();
           exitBtn.getScene().setRoot(rootLayout);
           
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//
	@FXML
	private void handlePlayBtnAction(ActionEvent event) {
		finished();
	}
	//
	@FXML
	private void handleEnterBtnAction(ActionEvent event) {
		String input = answerField.getText();
		if(isCorrect(input)) {
			score = score +1;
			scoreLabel.setText(Integer.toString(score));
		}
		questionNumber = questionNumber + 1;
		
		if (questionNumber > 10 ) {
			finished();
		}
			
		getNextQuestion(questionNumber);
		questionN.setText("Question " + Integer.toString(questionNumber));
	}
	
	
	
	
	
	
	private void getNextQuestion(int questionNumber) {
		String command = "awk 'NR=="+questionNumber+"' QuizList.txt";
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);		
				
		try {
			Process process = pb.start(); 
			process.waitFor();
			
			
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			String line = stdoutBuffered.readLine();
			
			if (line == null) {
				finished();
			} else {
				String name = line.substring(0,line.indexOf(",")-1).trim();
				answer = line.substring(line.indexOf(",")+1).trim();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}
	
	@FXML
	public void initialize() {
		questionNumber = 1;
		generateQuesitonList();
		getNextQuestion(questionNumber);
		
	}

	private void generateQuesitonList() {
		String command = " shuf -n 10 creationTermList.txt > QuizList.txt";
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);		
		try {
			Process searchProcess = pb.start(); 
			searchProcess.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
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
