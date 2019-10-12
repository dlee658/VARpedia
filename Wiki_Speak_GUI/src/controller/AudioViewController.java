package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.Main;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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

	private String term;
	
	@FXML
	private void handleHomeBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("mainMenu.fxml"));
			rootLayout = loader.load();
			homeBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleNextBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
			audioCreation();
			
			if(checkBox.isSelected()) {
				
				String audio = "\"Audio" + File.separatorChar +"music" +term +".wav\"";
				String audioC = "\"Audio" + File.separatorChar +term +".wav\"";
				String cmd = "ffmpeg -i "+audio+" -i backgroundMusic.wav -filter_complex amerge=inputs=2 -ac 2 "+audioC;
				
				ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
				Process a = pb.start();
				a.waitFor();
			}
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("retrieveImage.fxml"));
			rootLayout = loader.load();
			nextBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleBackBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("mainMenu.fxml"));
			rootLayout = loader.load();
			nextBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void initialize() {
		voiceCB.setValue("Male");
		voiceCB.getItems().addAll("Male", "NZ Guy", "Posh Lady");
		initializeResultArea();
		numberTxt = 0;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("term.txt"));
			term = reader.readLine();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void audioCreation() {
		String audio = "\"Audio" + File.separatorChar + term +".wav\"";
		if((checkBox.isSelected())) {
			audio = "\"Audio" + File.separatorChar +"music" +term +".wav\"";
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

	private void initializeResultArea() {
		File text = new File("Audio" + File.separatorChar + "audio_text.txt");
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(text));
			String line = reader.readLine().trim();
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
			ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "echo \""+selectedPart+ "\" | festival --tts");
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
			msg.setText("Audio: " + term + numberTxt + " saved");
			msg.setTextFill(Color.DARKGREEN);
			nextBtn.setDisable(false);

		}					
	}

	private void audioChunkCreation(String voice) {

		String audio = "\"Audio" + File.separatorChar + term +numberTxt+ ".wav\"";
		String text = "\"Audio" + File.separatorChar + term + numberTxt+".txt\"";

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

	protected void createText(String selectedText) {
		String cmd = "echo \"" + selectedText + "\"";
		ProcessBuilder pb = new ProcessBuilder("bash","-c",cmd);
		pb.redirectOutput(new File("Audio" + File.separatorChar + term +numberTxt+ ".txt"));

		try {
			Process process = pb.start();
			process.waitFor();

		}
		catch(Exception e) {

		}

	}

}
