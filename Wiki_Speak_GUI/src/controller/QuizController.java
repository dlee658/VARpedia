package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import application.Main;
import helper.Answer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;;

/**this page is for quiz that allow user to play game with previous creation*/
public class QuizController {
	@FXML 
	private Button exitBtn;

	@FXML
	private Label questionN;

	@FXML
	private MediaView mediaPlayer;

	@FXML
	private Button playBtn;

	@FXML
	private Button enterBtn;
	
	@FXML
	private Button helpBtn;
	
	@FXML
	private VBox helpBox;
	
	@FXML
	private BorderPane quizPane;

	@FXML
	private TextField answerField;
	
	private boolean helpOn = false;

	private String answer;

	private int questionNumber;
	private int score = 0;

	private MediaPlayer mp;
	private List<Answer> answerList = new ArrayList<Answer>();

	/**when this page been opened, return all value to initial value*/
	@FXML
	public void initialize() {
		questionNumber = 1;
		generateQuesitonList();
		getNextQuestion(questionNumber);
		helpBtn.fire();

		

	}
	
	/**
	 * Button to get the help for this page
	*/	
	@FXML
	private void handleHelpBtnAction(ActionEvent event) {
		helpOn  = !helpOn;
		if (helpOn) {
			pauseMP();
			quizPane.setDisable(true);
			helpBox.setVisible(true);
			helpBtn.setText("X");
		} else {
			playMP();
			quizPane.setDisable(false);
			helpBox.setVisible(false);
			helpBtn.setText("?");
		}

	}

	private void playMP() {
		mp.play();
		playBtn.setText("||");
		
	}

	private void pauseMP() {
		mp.pause();
		playBtn.setText(" ▷");
		
	}

	/**when exit, stop the video*/
	@FXML
	private void handleExitBtnAction(ActionEvent event) {
		pauseMP();
		if (questionNumber -1 == 0) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("mainMenu.fxml"));
				enterBtn.getScene().setRoot(loader.load());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			finished();
		}
	}

	/**play and stop button*/
	@FXML
	private void handlePlayBtnAction(ActionEvent event) {
		Status status = mp.getStatus();

		if ( status == Status.PAUSED || status == Status.READY || status == Status.STOPPED)	{
			playMP();
		} else {
			pauseMP();
		}
	}

	/**if user got the question correct, increment the score and 10 question total*/
	@FXML
	private void handleEnterBtnAction(ActionEvent event) {
		pauseMP();
		String input = answerField.getText();
		if(isCorrect(input)) {
			score = score + 1;
		}
		answerList.add(new Answer(input, answer));
		questionNumber = questionNumber + 1;

		if (questionNumber > 10 ) {
			finished();
		} else {
			answerField.clear();
			getNextQuestion(questionNumber);
			questionN.setText("Question " + Integer.toString(questionNumber));
		}

	}

	/**enable keyboard*/
	@FXML
	private void handleEnterKeyAction(KeyEvent key) {
		if (key.getCode().equals(KeyCode.ENTER)){
			enterBtn.fire();
		}
	}

	/**get next question using bash commnad*/
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
				playMP();
				mediaPlayer.setMediaPlayer(mp);
				
				mp.setOnEndOfMedia(new Runnable() {
					public void run() {
						playBtn.setText("↺");
						mp.seek(mp.getStartTime());		
						mp.pause();
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**generate question list of the quiz using bash command*/
	private void generateQuesitonList() {
		String command = "sed -i '/^[[:blank:]]*$/ d' creationTermList.txt; shuf -n 10 creationTermList.txt > QuizList.txt";
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);		
		try {
			Process searchProcess = pb.start(); 
			searchProcess.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**check whether user got the question correct or not*/
	public boolean isCorrect(String term) {
		if(term.equalsIgnoreCase(answer)){
			return true;
		}
		else {
			return false;
		}
	}

	/**when quiz finished, show user the result by result page*/
	public void finished() {
		try {
			pauseMP();	
			ResultPageController controller = new ResultPageController(score, questionNumber-1, answerList);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("quizResult.fxml"));
			loader.setController(controller);			
			enterBtn.getScene().setRoot(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}


	}



}
