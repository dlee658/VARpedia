package controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import application.Main;
import helper.Answer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer.Status;

public class ResultPageController {
	@FXML 
	private Button menuBtn;

	@FXML 
	private Button retakeBtn;
	
	@FXML 
	private Label scoreLabel;

	private int _score;

	private int _questions;
	
	@FXML
	private TableView answerTable;
	
	@FXML
	private TableColumn yourAnswerColumn;
	
	@FXML
	private TableColumn correctAnswerColumn;
	
	@FXML
	private TableColumn correctnessColumn;
	
	
	private ObservableList<Answer> _answerList;
	
	
	public ResultPageController(int score, int questions, List<Answer> answerList) {
		_score = score;
		_questions = questions;
		_answerList = FXCollections.observableList(answerList); 
	}
	
	@FXML
	public void initialize() {
		scoreLabel.setText(_score+ "/"+ _questions);
		initializeTable();
	}
	
	private void initializeTable() {

		yourAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("yourAnswer"));
		
		correctAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));

		correctnessColumn.setCellValueFactory(new PropertyValueFactory<>("isCorrect"));
		
		answerTable.getColumns().setAll(yourAnswerColumn, correctAnswerColumn,correctnessColumn);
		
		answerTable.setItems(_answerList);

		
	}
	

	
	@FXML
	private void handleMenuBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("mainMenu.fxml"));
			Pane rootLayout = loader.load();
			menuBtn.getScene().setRoot(rootLayout);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleRetakeBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("quizView.fxml"));
			Pane rootLayout = loader.load();
			menuBtn.getScene().setRoot(rootLayout);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
