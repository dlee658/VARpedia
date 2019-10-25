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
	private ScrollPane scrollPane;
	
	@FXML 
	private Label scoreLabel;

	private int _score;

	private int _questions;
	
	private TableView<Answer> answerTable;
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

		
		answerTable = new TableView<Answer>();

		TableColumn<Answer, String> youAnswerColumn = new TableColumn<>("Your Answer");
		youAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("yourAnswer"));
		

		TableColumn<Answer, String> correctAnswerColumn = new TableColumn<>("Correct Answer");
		correctAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));

		
		TableColumn<Answer, Boolean> correctnessColumn = new TableColumn<>("Correct/Wrong");
		correctnessColumn.setCellValueFactory(new PropertyValueFactory<>("isCorrect"));
		
		answerTable.getColumns().setAll(youAnswerColumn, correctAnswerColumn,correctnessColumn);
		
		answerTable.setItems(_answerList);
		
		scrollPane.setContent(answerTable);
		answerTable.setPrefWidth(scrollPane.getPrefWidth());
		youAnswerColumn.setPrefWidth(answerTable.getPrefWidth()/3);
		correctAnswerColumn.setPrefWidth(answerTable.getPrefWidth()/3);
		correctnessColumn.setPrefWidth(answerTable.getPrefWidth()/3);
		youAnswerColumn.setResizable(false);
		correctAnswerColumn.setResizable(false);
		correctnessColumn.setResizable(false);
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
