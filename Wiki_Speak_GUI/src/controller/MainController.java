package controller;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

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

	private Pane rootLayout;

	/**
	 * go to quiz page
	 */
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
	
	/** start create creation, so go to search page*/
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
	
	/**go to view creation page*/
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

	/**go to helper page that give user instruction*/
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
