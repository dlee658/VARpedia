package helper;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
	
	public String getYourAnswer() {
		return yourAnswerProperty().get();
	}
    
	public StringProperty yourAnswerProperty() { 
        if (yourAnswer == null) {
        	yourAnswer = new SimpleStringProperty("yourAnswer");
        }
        return yourAnswer; 
    }
	
	public String getCorrectAnswer() {
		return correctAnswerProperty().get();
	}
    
	public StringProperty correctAnswerProperty() { 
        if (correctAnswer == null) {
        	correctAnswer = new SimpleStringProperty("correctAnswer");
        }
        return correctAnswer; 
    }
	
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
