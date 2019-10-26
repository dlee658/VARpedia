package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class MainController {
	@FXML 
	private Button createBtn;

	@FXML 
	private Button viewBtn;

	@FXML 
	private Button quizBtn;

	@FXML 
	private Button helpBtn;

	private Pane rootLayout;

	@FXML
	public void initialize() {
		try {
			String cmd = "ls Creations |wc -l";
			ProcessBuilder builder = new ProcessBuilder("bash","-c",cmd);
			Process process = builder.start();

			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			String line = stdoutBuffered.readLine();
			
			if (line.equals("0")) {
				quizBtn.setDisable(true);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@FXML
	private void handleQuizBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("quizView.fxml"));
			rootLayout = loader.load();
			quizBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleCreateBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("searchPage.fxml"));
			rootLayout = loader.load();
			createBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleViewBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("viewView.fxml"));
			rootLayout = loader.load();
			viewBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleHelpBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("helpView.fxml"));
			rootLayout = loader.load();
			quizBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
