package helper;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class SceneChanger {

	public static void changeScene(Object constructor, String fxml, Button btn) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource(fxml));
			if (constructor != null) {
				loader.setController(constructor);
			}
			btn.getScene().setRoot(loader.load());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
