package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;;

public class QuizController {
	@FXML 
	private Button exitBtn;
	
	@FXML
	private Label questionN;
	
	@FXML
	private MediaView mediaPlayer;
	@FXML
	private ImageView playImage;


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

	private MediaPlayer mp;
	
	@FXML
	public void initialize() {
		questionNumber = 1;
		generateQuesitonList();
		getNextQuestion(questionNumber);
		
		mp.setOnEndOfMedia(new Runnable() {
			public void run() {
				playImage.setImage(new Image("controller"+File.separatorChar+"replay.png"));
				mp.seek(mp.getStartTime());		
				mp.pause();
			}
		});
			
	}

	@FXML
	private void handleExitBtnAction(ActionEvent event) {
		finished();
	}
	
	@FXML
	private void handlePlayBtnAction(ActionEvent event) {
		Status status = mp.getStatus();

		if ( status == Status.PAUSED || status == Status.READY || status == Status.STOPPED)	{
			mp.play();
			playImage.setImage(new Image("controller"+File.separatorChar+"pause.png"));
		} else {
			mp.pause();
			playImage.setImage(new Image("controller"+File.separatorChar+"play.png"));
		}
	}
	
	@FXML
	private void handleEnterBtnAction(ActionEvent event) {
		mp.pause();
		String input = answerField.getText();
		if(isCorrect(input)) {
			score = score + 1;
			scoreLabel.setText(Integer.toString(score));
		}
		questionNumber = questionNumber + 1;
		
		if (questionNumber > 10 ) {
			finished();
		}
		answerField.clear();
		getNextQuestion(questionNumber);
		questionN.setText("Question " + Integer.toString(questionNumber));
	}
	
	@FXML
	private void handleEnterKeyAction(KeyEvent key) {
		if (key.getCode().equals(KeyCode.ENTER)){
			enterBtn.fire();
		}
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
				String name = line.substring(0,line.indexOf(",")).trim();
				File file = new File("Video" + File.separatorChar + name + ".mp4");
				answer = line.substring(line.indexOf(",")+1).trim();
				Media video = new Media(file.toURI().toString());
				mp = new MediaPlayer(video);
				mp.setAutoPlay(true);
				mediaPlayer.setMediaPlayer(mp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
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
