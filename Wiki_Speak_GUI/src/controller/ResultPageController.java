package controller;

import java.util.List;

import helper.Answer;
import helper.SceneChanger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**this page gives user the result of the quiz*/
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

	/**get the inputs from previous page*/
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

	/**by using tableview, give user the correct answer and user's typed answer and show whether it is correct or not*/
	private void initializeTable() {


		yourAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("yourAnswer"));

		correctAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));

		correctnessColumn.setCellValueFactory(new PropertyValueFactory<>("isCorrect"));

		answerTable.getColumns().setAll(yourAnswerColumn, correctAnswerColumn,correctnessColumn);

		answerTable.setItems(_answerList);

	}

	/**return to main page*/
	@FXML
	private void handleMenuBtnAction(ActionEvent event) {
		SceneChanger.changeScene(null, "mainMenu.fxml", menuBtn);
	}

	/**play quiz again*/
	@FXML
	private void handleRetakeBtnAction(ActionEvent event) {
		SceneChanger.changeScene(null, "quizView.fxml", retakeBtn);
	}
}
