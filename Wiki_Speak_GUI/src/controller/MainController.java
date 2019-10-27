package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import helper.BashCommand;
import helper.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * this is controller for the main page
 * */
public class MainController {
	@FXML 
	private Button createBtn;

	@FXML 
	private Button viewBtn;

	@FXML 
	private Button quizBtn;

	@FXML 
	private Button helpBtn;

	private boolean helpOn = false;
	
	@FXML
	private VBox mainVB;

	@FXML
	private VBox helpBox;

	@FXML
	public void initialize() {
		try {
			String cmd = "ls Creations |wc -l";
			Process process = BashCommand.runCommand(cmd);

			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			String line = stdoutBuffered.readLine();

			if (line.equals("0")) {
				viewBtn.setDisable(true);
				quizBtn.setDisable(true);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * go to quiz page
	 */
	@FXML
	private void handleQuizBtnAction(ActionEvent event) {
		SceneChanger.changeScene(null, "quizView.fxml", quizBtn);
	}

	/** start create creation, so go to search page*/

	@FXML
	private void handleCreateBtnAction(ActionEvent event) {
		SceneChanger.changeScene(null, "searchPage.fxml", createBtn);
	}


	/**go to view creation page*/
	@FXML
	private void handleViewBtnAction(ActionEvent event) {
		SceneChanger.changeScene(null, "viewView.fxml", viewBtn);
	}


	/**go to helper page that give user instruction*/
	@FXML
	private void handleHelpBtnAction(ActionEvent event) {
		helpOn  = !helpOn;
		if (helpOn  ) {
			mainVB.setDisable(true);
			helpBox.setVisible(true);
			helpBtn.setText("X");
		} else {
			mainVB.setDisable(false);
			helpBox.setVisible(false);
			helpBtn.setText("?");
		}

	}
}
