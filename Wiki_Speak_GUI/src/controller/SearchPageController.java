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
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/**page that search term using wikit command*/
public class SearchPageController {
	@FXML 
	private Button homeBtn;

	@FXML 
	private Button searchBtn;

	@FXML 
	private Button cancelBtn;
	
	@FXML 
	private Button helpBtn;
	
	@FXML 
	private VBox helpBox;
		
	@FXML 
	private VBox searchVB;

	@FXML 
	private Label msg;

	@FXML 
	private TextField searchField;

	@FXML 
	private ProgressIndicator searchIndicator;

	private Task<Boolean> searchTask;

	private boolean helpOn = false;

	/**return to home button*/
	@FXML
	private void handleHomeBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("mainMenu.fxml"));
			homeBtn.getScene().setRoot(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**button that cancel searching*/
	@FXML
	private void handleCancelBtnAction(ActionEvent event) {
		searchTask.cancel();
		searchIndicator.setVisible(false);
		searchBtn.setDisable(false);
		cancelBtn.setDisable(true);
	}

/**button that search with given term*/
	@FXML
	private void handleSearchBtnAction(ActionEvent event) {
		String command = "rm Audio/*.txt; rm  Audio/*.wav; rm *.jpg";
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);			
		try {
			Process searchProcess = pb.start(); 
			searchProcess.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		//enable progress indicator and cancel btn
		searchIndicator.setDisable(false);
		searchIndicator.setVisible(true);
		searchBtn.setDisable(true);
		cancelBtn.setDisable(false);
		//do searching process
		String term = searchField.getText();
		search(term);
	}

	@FXML
	private void handleHelpBtnAction(ActionEvent event) {
		helpOn  = !helpOn;
		if (helpOn) {
			searchVB.setDisable(true);
			helpBox.setVisible(true);
			helpBtn.setText("X");
		} else {
			searchVB.setDisable(false);
			helpBox.setVisible(false);
			helpBtn.setText("?");
		}
	}
	
	/**enable keyboard enter*/
	@FXML
	private void handleEnterKeyAction(KeyEvent key) {
		if (key.getCode().equals(KeyCode.ENTER)){
			searchBtn.fire();
		}
	}

	/**check whether that user typed something and allow search*/
	private void search(String term) {
		term = searchField.getText().trim();

		if (term.isEmpty()) {
			searchIndicator.setVisible(false);
			searchBtn.setDisable(false);
			cancelBtn.setDisable(true);
			msg.setText("Please enter a term");
			return;
		}
		searchBtn.setDisable(true);
		getSearchResult(term);

	}

	/**
	 * Returns the wikit result of the entered term and displays result in result area.
	 */
	private void getSearchResult(String term) {
		String command = "wikit " + term + " | sed 's/\\. /&\\n/g'";
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

		File text = new File("Audio" + File.separatorChar + "audio_text.txt");
		pb.redirectOutput(text);

		ExecutorService worker = Executors.newSingleThreadExecutor(); 
		searchTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				try {
					Process searchProcess = pb.start(); 
					searchProcess.waitFor();

				}catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}
		};

		worker.submit(searchTask);
		
		//when finished searching
		searchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				BufferedReader reader;
				try {
					reader = new BufferedReader(new FileReader(text));
					String line = reader.readLine().trim();
					//if there are no result in term, display it to user
					if (line.equals(term + " not found :^(")) {
						msg.setText(term + " not found, please try again");
					} else { //display it to user in result area
						AudioViewController avc = new AudioViewController(searchField.getText(),text);
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(Main.class.getResource("audioView.fxml"));
						loader.setController(avc);
						searchBtn.getScene().setRoot(loader.load());
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				searchBtn.setDisable(false);
				cancelBtn.setDisable(true);
				searchIndicator.setVisible(false);
			}
		});


	}


}
