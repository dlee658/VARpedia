package helper;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Class for the answer to the quiz
 *
 */
public class Answer {

	private Boolean _isCorrect;
	private SimpleStringProperty yourAnswer;
	private SimpleStringProperty correctAnswer;
	private SimpleStringProperty isCorrect;

	public Answer(String yourAnswer, String correctAnswer) {
		yourAnswerProperty().set(yourAnswer);
		correctAnswerProperty().set(correctAnswer);
		_isCorrect = yourAnswer.equalsIgnoreCase(correctAnswer);
		
		if (_isCorrect) {
			isCorrectProperty().setValue("Correct");
		} else {
			isCorrectProperty().setValue("Wrong");
		}
	}
	
	/**
	 * Returns the users given answer
	 */
	public String getYourAnswer() {
		return yourAnswerProperty().get();
	}
    
	public StringProperty yourAnswerProperty() { 
        if (yourAnswer == null) {
        	yourAnswer = new SimpleStringProperty("yourAnswer");
        }
        return yourAnswer; 
    }
	/**
	 * Returns the correct answer for the question
	 */
	public String getCorrectAnswer() {
		return correctAnswerProperty().get();
	}
    
	public StringProperty correctAnswerProperty() { 
        if (correctAnswer == null) {
        	correctAnswer = new SimpleStringProperty("correctAnswer");
        }
        return correctAnswer; 
    }
	
	/**
	 * Returns whether the users answer wa correct or not
	 */
	public String getIsCorrect() {
		return isCorrectProperty().get();
	}
    
	public StringProperty isCorrectProperty() { 
		 if (isCorrect == null) {
			 isCorrect = new SimpleStringProperty();
	        }
        return isCorrect; 
    }
	
}
