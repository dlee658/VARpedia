package controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.Main;
import helper.DownloadImageTask;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
	private Pane mainAudioPane;

	@FXML 
	private ChoiceBox<String> voiceCB;

	private Pane rootLayout;

	private int numberTxt;

	private String _term;

	private String _resultAreaText;
	
	private boolean helpOn = false;

	private Process previewProcess;

	public AudioViewController(String term, String resultAreaText) {
		_term = term;
		_resultAreaText = resultAreaText;
	}

	/**
	 * set voice options
	*/	
	@FXML
	public void initialize() {
		voiceCB.setValue("Male");
		voiceCB.getItems().addAll("Male", "NZ Male", "NZ Female");
		resultArea.setText(_resultAreaText);
		numberTxt = 0;


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
			try {
				// Load root layout from fxml file.
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("mainMenu.fxml"));
				Pane rootLayout = loader.load();
				homeBtn.getScene().setRoot(rootLayout);

			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}
	
	@FXML
	private void handleHelpBtnAction(ActionEvent event) {
		if (helpOn) {
			helpBox.setVisible(true);
			helpBtn.setText("X");
		} else {
			helpBox.setVisible(false);
			helpBtn.setText("?");
		}
		helpOn = !helpOn;
		
	}

	/**
	 * button to go to the next scene
	 */
	@FXML
	private void handleNextBtnAction(ActionEvent event) {
		try {

			audioCreation();

			//Combine audio with background music
			if(checkBox.isSelected()) {

				String audio = "\"Audio" + File.separatorChar +"music" +_term +".wav\"";
				String audioC = "\"Audio" + File.separatorChar +_term +".wav\"";
				String cmd = "ffmpeg -i "+audio+" -i backgroundMusic.wav -filter_complex amerge=inputs=2 -ac 2 "+audioC;

				ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
				Process audioProcess = pb.start();
				audioProcess.waitFor();
			}

			
				LoadingController avc = new LoadingController(_term);
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("Loading.fxml"));
				loader.setController(avc);
				nextBtn.getScene().setRoot(loader.load());
				
			} 
		 catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * button for go back to previous page
	*/	
	@FXML
	private void handleBackBtnAction(ActionEvent event) {
		try {
			previewProcess.destroyForcibly();
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("searchPage.fxml"));
			rootLayout = loader.load();
			nextBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * button for preview the selected text with selected voice; default voice is kal_diphone
	*/	
	@FXML
	private void handlePreviewBtnAction(ActionEvent event) {
		String selectedPart = resultArea.getSelectedText();
		int wordCount = selectedPart.split("\\s+").length;

		//can't select nothing and preview it
		if (selectedPart.isEmpty()) {
			msg.setText("Please highlight some text");
		}
		//word limit is 40 words
		else if (wordCount > 40) { 
			msg.setText("Selection exceeds 40 words, try again");						
		}
		else {
			msg.setText("");
			String voice = voiceCB.getValue().toString();
			if(voice.equals("NZ Male")) {
				voice = "voice_akl_nz_jdt_diphone";		
			}
			else if(voice.equals("NZ Female")) {
				voice = "voice_akl_nz_cw_cg_cg";		
			}				
			else {
				voice = "voice_kal_diphone";	
			}
			//preview voice with selected voice
			ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "echo $'("+voice+") \n(SayText \""+selectedPart+ "\")' > preview.scm; festival -b preview.scm");
			try {
				previewProcess = pb.start();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * button for saving audio for selected text
	*/	
	@FXML
	private void handleSaveBtnAction(ActionEvent event) {
		String selectedPart = resultArea.getSelectedText();
		int wordCount = selectedPart.split("\\s+").length;
		msg.setTextFill(Color.INDIANRED);
		//saving word without select anything disabled
		if (selectedPart.isEmpty()) {
			msg.setText("Please highlight some text");
		} 
		//40 words limit
		else if (wordCount > 40) {
			msg.setText("Selection exceeds 40 words, try again");

		}
		else {
			//increment the number of audio been created
			numberTxt = numberTxt +1;
			String voice = voiceCB.getValue().toString();
			//create a text file for audio with given text
			createText(selectedPart);
			//create a wav file with text file
			audioChunkCreation(voice);

			if (numberTxt > 0) {
				nextBtn.setDisable(false);
			}
		}					
	}

/**
 * method that check whether wav file is 0 byte or not
 * @return boolean: false if file is empty, true if some file been created
 */
	private boolean isValidAudioChunk(String audioPath) {
		File file = new File(audioPath);
		file.exists();
		if (file.length() == 0) {
			//file.delete();
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
		//for GUI to not be froze by using workers
		ExecutorService createWorker = Executors.newSingleThreadExecutor(); 
		Task<File> task = new Task<File>() {
			@Override
			protected File call() throws Exception {
				try {
					String voiceFile = "\"Voice" + File.separatorChar + voice + ".scm\"";
					String cmd = "text2wave -o " + audio + " " + text + " -eval "+ voiceFile + "&> audiostatus.txt";					
					ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
					Process audioProcess = pb.start();
					audioProcess.waitFor();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;	
			}
		};
		createWorker.submit(task);
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
		//when voice successfully created
			@Override
			public void handle(WorkerStateEvent arg0) {
				//check that file is 0byte or not using method
				if (!isValidAudioChunk("Audio" + File.separatorChar + _term +numberTxt+ ".wav")) {
					//if file is 0byte, than display message to user and decrement text number
					msg.setText("Text highlighted contains an unreadable character");
					msg.setTextFill(Color.INDIANRED);
					numberTxt = numberTxt-1;
				}
				else {
					msg.setText("Audio: " + _term + numberTxt + " saved");
					msg.setTextFill(Color.DARKGREEN);
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
		try {
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
			Process audioProcess = pb.start();

			audioProcess.waitFor();
		} catch (InterruptedException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * method that create text with selected text
	 */
	private void createText(String selectedText) {
		String cmd = "echo \"" + selectedText + "\"";
		ProcessBuilder pb = new ProcessBuilder("bash","-c",cmd);
		pb.redirectOutput(new File("Audio" + File.separatorChar + _term +numberTxt+ ".txt"));

		try {
			Process process = pb.start();
			process.waitFor();

		}
		catch(Exception e) {

		}

	}

}
