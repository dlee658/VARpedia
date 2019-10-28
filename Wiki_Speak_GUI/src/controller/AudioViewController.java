package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import helper.BashCommand;
import helper.SceneChanger;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class AudioViewController {
	@FXML 
	private Button homeBtn;

	@FXML 
	private Button previewBtn;

	@FXML
	private CheckBox checkBox;

	@FXML 
	private Button saveBtn;
	
	@FXML 
	private Button backBtn;

	@FXML 
	private Button nextBtn;

	@FXML 
	private Button helpBtn;

	@FXML 
	private TextArea resultArea;

	@FXML
	private Label msg;

	@FXML
	private VBox helpBox;
	
	@FXML
	private ProgressIndicator saveIndicator;

	@FXML
	private BorderPane audioPane;

	@FXML 
	private ChoiceBox<String> voiceCB;

	private int numberTxt;

	private String _term;

	private File _textFile;

	private boolean helpOn = false;

	private MediaPlayer audioPlayer;
	
	private final String TEMP = "Temp" + File.separatorChar;

	public AudioViewController(String term, File textfile) {
		_term = term;
		_textFile = textfile;
	}
	
	

	/**
	 * set voice options
	 */	
	@FXML
	public void initialize() {
		initializeTextArea();
		voiceCB.setValue("Male");
		voiceCB.getItems().addAll("Male", "NZ Male", "NZ Female");
		numberTxt = 0;

	}

	private void initializeTextArea() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(_textFile));
			String line = reader.readLine().trim();
			//display the search result to user
			resultArea.setText(line);

			while ((line = reader.readLine()) != null ) {
				resultArea.appendText(line);
			}
			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * set home button to go back to main menu
	 */	
	@FXML
	private void handleHomeBtnAction(ActionEvent event) {
		//warn user and ask for the confirmation to go back to main menu
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Home");
		alert.setHeaderText("Are you sure you want to leave?");
		alert.setContentText("Current creation will not be saved.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			SceneChanger.changeScene(null, "mainMenu.fxml", homeBtn);
		} 
	}

	@FXML
	private void handleHelpBtnAction(ActionEvent event) {
		helpOn = !helpOn;
		if (helpOn) {
			audioPane.setDisable(true);
			helpBox.setVisible(true);
			helpBtn.setText("X");
		} else {
			audioPane.setDisable(false);
			helpBox.setVisible(false);
			helpBtn.setText("?");
		}
	}

	/**
	 * button to go to the next scene
	 */
	@FXML
	private void handleNextBtnAction(ActionEvent event) {
		//Combines audio chunks together
		audioCreation();

		//Combine audio with background music
		if(checkBox.isSelected()) {
			String audio = "\"Audio" + File.separatorChar +"music" +_term +".wav\"";
			String audioC = "\"Audio" + File.separatorChar +_term +".wav\"";
			String cmd = "ffmpeg -i "+audio+" -i backgroundMusic.wav -filter_complex amerge=inputs=2 -ac 2 "+audioC;
			BashCommand.runCommand(cmd);
		}

		//Move to next page
		LoadingController controller = new LoadingController(_term);
		SceneChanger.changeScene(controller, "Loading.fxml", nextBtn);
	}

	/**
	 * button for go back to previous page
	 */	
	@FXML
	private void handleBackBtnAction(ActionEvent event) {
		SceneChanger.changeScene(null, "searchPage.fxml", backBtn);
	}

	/**
	 * button for preview the selected text with selected voice; default voice is kal_diphone
	 */	
	@FXML
	private void handlePreviewBtnAction(ActionEvent event) {	
		//If preview is still running
		if (audioPlayer != null && audioPlayer.getStatus() == Status.PLAYING) {
			audioPlayer.stop();
			previewBtn.setText("Preview");
			return;
		}
		//Checks if  highlighted is valid and previews it
		if (isValidHighlight()) {
			msg.setText("");
			String voice = voiceCB.getValue().toString();
			String voiceFile = "\"Voice" + File.separatorChar + voice + ".scm\"";
			String cmd = "echo \"" + resultArea.getSelectedText() + "\" > " + TEMP + "preview.txt; text2wave -o "+TEMP+"preview.wav "+TEMP+"preview.txt -eval "+ voiceFile + "&> audiostatus.txt";					
			BashCommand.runCommand(cmd);

			File file = new File(TEMP + "preview.wav");
			Media audio = new Media(file.toURI().toString());
			audioPlayer = new MediaPlayer(audio);
			audioPlayer.setAutoPlay(true);
			previewBtn.setText("Stop");

			audioPlayer.setOnEndOfMedia(new Runnable() {
				public void run() {
					previewBtn.setText("Preview");
				}
			});

		} 

	}

	/**
	 * button for saving audio for selected text
	 */	
	@FXML
	private void handleSaveBtnAction(ActionEvent event) {
		msg.setText("");
		saveIndicator.setVisible(true);
		if (isValidHighlight()) {
			//increment the number of audio been created
			numberTxt = numberTxt +1;
			String voice = voiceCB.getValue().toString();
			//create a text file for audio with given text
			createText(resultArea.getSelectedText());
			//create a wav file with text file
			audioChunkCreation(voice);
		} else {
			saveIndicator.setVisible(false);
		}
	}

	/**
	 * Checks if the text highlighted is valid, not blank or exceed word limit
	 */
	private boolean isValidHighlight() {
		String selectedPart = resultArea.getSelectedText();
		int wordCount = selectedPart.split("\\s+").length;
	
		//can't select nothing and preview it
		if (selectedPart.isEmpty()) {
			msg.setText("Please highlight some text");
			return false;
		}
		//word limit is 40 words
		else if (wordCount > 40) { 
			msg.setText("Selection exceeds 40 words, try again");	
			return false;
		}
		return true;
	}

	/**
	 * method that check whether wav file is 0 byte or not
	 * @return boolean: false if file is empty, true if some file been created
	 */
	private boolean isValidAudioChunk(String audioPath) {
		File file = new File(audioPath);
		file.exists();
		if (file.length() == 0) {
			file.delete();
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Saves the highlighted text as audio
	 * @param voice
	 */
	private void audioChunkCreation(String voice) {
		//files directory
		String audio = "\"Audio" + File.separatorChar + _term +numberTxt+ ".wav\"";
		String text = "\"Audio" + File.separatorChar + _term + numberTxt+".txt\"";
		
		//Run task to save audio chunks to a wav file
		ExecutorService createWorker = Executors.newSingleThreadExecutor(); 
		Task<File> task = new Task<File>() {
			@Override
			protected File call() throws Exception {
				String voiceFile = "\"Voice" + File.separatorChar + voice + ".scm\"";
				String cmd = "text2wave -o " + audio + " " + text + " -eval "+ voiceFile + "&> audiostatus.txt";					
				BashCommand.runCommand(cmd);
				return null;	
			}
		};
		createWorker.submit(task);

		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			//when voice successfully created
			@Override
			public void handle(WorkerStateEvent arg0) {
				saveIndicator.setVisible(false);
				//check that file is 0byte or not using method
				if (!isValidAudioChunk("Audio" + File.separatorChar + _term +numberTxt+ ".wav")) {
					//if file is 0byte, than display message to user and decrement text number
					msg.setText("Text highlighted contains an unreadable character");
					numberTxt = numberTxt-1;
				}
				else {
					msg.setText("Audio: " + _term + numberTxt + " saved");
				}
				
				if (numberTxt > 0) {
					nextBtn.setDisable(false);
				}

			}

		}); 
	}

	/**
	 * Combines all the audio chunks into one
	 */
	private void audioCreation() {
		String audio = "\"Audio" + File.separatorChar + _term +".wav\"";
		if((checkBox.isSelected())) {
			audio = "\"Audio" + File.separatorChar +"music" +_term +".wav\"";
		}
		//bash command that combine all the wav files into one single wav file
		String cmd = "for f in Audio/*.wav; do echo \"file '$f'\" >> mylist.txt ; done ; ffmpeg -safe 0 -y -f concat -i mylist.txt -c copy " + audio + "; rm mylist.txt";
		BashCommand.runCommand(cmd);
	}

	/**
	 * method that create text with selected text
	 */
	private void createText(String selectedText) {
		String cmd = "echo \"" + selectedText + "\" > \"Audio" + File.separatorChar + _term + numberTxt + ".txt\"";
		BashCommand.runCommand(cmd);
	}

}
