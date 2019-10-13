package controller;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class HelpController {
	
	@FXML 
	private Button homeBtn;

	
	@FXML
	private void handleHomeBtnAction(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("mainMenu.fxml"));
			homeBtn.getScene().setRoot(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
