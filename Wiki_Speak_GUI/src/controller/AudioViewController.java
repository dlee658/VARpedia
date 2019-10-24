package controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.DownloadImageTask;
import application.Main;
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
	private TextArea resultArea;

	@FXML
	private Label msg;

	@FXML 
	private ChoiceBox<String> voiceCB;

	private Pane rootLayout;

	private int numberTxt;

	private String _term;

	private String _resultAreaText;

	public AudioViewController(String term, String resultAreaText) {
		_term = term;
		_resultAreaText = resultAreaText;
	}

	@FXML
	public void initialize() {
		voiceCB.setValue("Male");
		voiceCB.getItems().addAll("Male", "NZ Guy", "Posh Lady");
		resultArea.setText(_resultAreaText);
		numberTxt = 0;


	}

	@FXML
	private void handleHomeBtnAction(ActionEvent event) {
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

			RetrieveImage controller = new RetrieveImage(_term);

			ExecutorService worker = Executors.newSingleThreadExecutor(); 
			DownloadImageTask dlTask = new DownloadImageTask(_term);
			worker.submit(dlTask);

			dlTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					try {

						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(Main.class.getResource("retrieveImage.fxml"));
						loader.setController(controller);

						nextBtn.getScene().setRoot(loader.load());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleBackBtnAction(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("searchPage.fxml"));
			rootLayout = loader.load();
			nextBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handlePreviewBtnAction(ActionEvent event) {
		String selectedPart = resultArea.getSelectedText();
		int wordCount = selectedPart.split("\\s+").length;
		msg.setTextFill(Color.INDIANRED);
		if (selectedPart.isEmpty()) {
			msg.setText("Please highlight some text");
		}
		else if (wordCount > 40) { 
			msg.setText("Selection exceeds 40 words, try again");						
		}
		else {
			msg.setText("");
			String voice = voiceCB.getValue().toString();
			if(voice.equals("Male")) {
				voice = "voice_kal_diphone";
			}
			else if(voice.equals("NZ Guy")) {
				voice = "voice_akl_nz_jdt_diphone";		
			}
			else if(voice.equals("Posh Lady")) {
				voice = "voice_akl_nz_cw_cg_cg";		
			}				
			else {
				voice = "voice_kal_diphone";	
			}
			ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "echo $'(voice_kal_diphone) \n(SayText \""+selectedPart+ "\")' > preview.scm; festival -b preview.scm");
			try {
				pb.start();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	@FXML
	private void handleSaveBtnAction(ActionEvent event) {
		String selectedPart = resultArea.getSelectedText();
		int wordCount = selectedPart.split("\\s+").length;
		msg.setTextFill(Color.INDIANRED);
		if (selectedPart.isEmpty()) {
			msg.setText("Please highlight some text");
		} 
		else if (wordCount > 40) {
			msg.setText("Selection exceeds 40 words, try again");

		}
		else {
			numberTxt = numberTxt +1;
			String voice = voiceCB.getValue().toString();
			createText(selectedPart);
			audioChunkCreation(voice);
			checkWav();
			
			// read temporaryTextFile, numberTxt -1 and display message that audio file not generated
			
			msg.setText("Audio: " + _term + numberTxt + " saved");
			msg.setTextFill(Color.DARKGREEN);

			
			if (numberTxt > 0) {
			nextBtn.setDisable(false);
			}
		}					
	}
	
	private void checkWav() {
		ExecutorService createWorker = Executors.newSingleThreadExecutor(); 
		Task<File> task = new Task<File>() {
			@Override
			protected File call() throws Exception {
				try {
		String audio = "\"Audio" + File.separatorChar + _term +numberTxt+ ".wav\"";
		String command = "if [  ! -s  "+audio+" ];	then rm "+audio+"; echo \"delete\" > temporaryTextFile.txt; else; echo \"not deleted\" > temporaryTextFile.txt; fi";
		ProcessBuilder pb1 = new ProcessBuilder("bash", "-c", command);
		Process audioProcess1 = pb1.start();
		audioProcess1.waitFor();	
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;	
			}
		};

		createWorker.submit(task);
		
		
		
		

	}
	
	
	

	/**
	 * Saves the highlighted text as audio
	 * @param voice
	 */
	private void audioChunkCreation(String voice) {

		String audio = "\"Audio" + File.separatorChar + _term +numberTxt+ ".wav\"";
		String text = "\"Audio" + File.separatorChar + _term + numberTxt+".txt\"";

		ExecutorService createWorker = Executors.newSingleThreadExecutor(); 
		Task<File> task = new Task<File>() {
			@Override
			protected File call() throws Exception {
				try {

					String voiceFile;
					String cmd;

					if(voice.equals("Male")) {
						voiceFile = "\"Voice" + File.separatorChar + "kal.scm\"";
					}
					else if(voice.equals("NZ Guy")) {
						voiceFile = "\"Voice" + File.separatorChar + "jdt.scm\"";			
					}
					else if(voice.equals("Posh Lady")) {
						voiceFile = "\"Voice" + File.separatorChar + "cw.scm\"";			
					}				
					else {
						voiceFile = "\"Voice" + File.separatorChar + "kal.scm\"";			
					}

					cmd = "text2wave -o " + audio + " " + text + " -eval "+ voiceFile;					

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
	}

	/**
	 * Combines all the audio chunks into one
	 */
	private void audioCreation() {
		String audio = "\"Audio" + File.separatorChar + _term +".wav\"";
		if((checkBox.isSelected())) {
			audio = "\"Audio" + File.separatorChar +"music" +_term +".wav\"";
		}
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
