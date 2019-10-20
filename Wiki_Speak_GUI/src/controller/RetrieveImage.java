package controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Optional;
import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class RetrieveImage {
	@FXML 
	private Button homeBtn;

	@FXML 
	private Button nextBtn;

	@FXML 
	private Button backBtn;

	@FXML
	private CheckBox image1CB;
	@FXML
	private CheckBox image2CB;

	@FXML
	private CheckBox image3CB;

	@FXML
	private CheckBox image4CB;

	@FXML
	private CheckBox image5CB;

	@FXML
	private CheckBox image6CB;

	@FXML
	private CheckBox image7CB;

	@FXML
	private CheckBox image8CB;

	@FXML
	private CheckBox image9CB;

	@FXML
	private CheckBox image10CB;
	@FXML
	private Button selectAll;
	@FXML
	private ImageView image1;
	@FXML
	private ImageView image2;
	@FXML
	private ImageView image3;
	@FXML
	private ImageView image4;
	@FXML
	private ImageView image5;
	@FXML
	private ImageView image6;
	@FXML
	private ImageView image7;
	@FXML
	private ImageView image8;
	@FXML
	private ImageView image9;
	@FXML
	private ImageView image10;

	private String _term;

	public RetrieveImage(String term) {
		_term = term;
	}

	@FXML
	public void initialize() {
		displayImages();
	}

	public void displayImages() {
		int i = 1;
		String filename;
		for(Field f: this.getClass().getDeclaredFields()) {
			if (f.getType().equals(ImageView.class)) {
				try {
					if (i == 10) {
						filename = _term+i+".jpg";
					} else {
						filename = _term+"0"+i+".jpg";
					}
					ImageView image = (ImageView) f.get(this);
					Image im = new Image("file:" + filename);
					image.setImage(im);
					i++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	@FXML
	private void handleSelectAllAction(ActionEvent event) {
		for(Field f: this.getClass().getDeclaredFields()) {
			if (f.getType().equals(CheckBox.class)) {
				try {
					CheckBox checkBox = (CheckBox) f.get(this);
					checkBox.setSelected(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}


	}

	@FXML
	private void handleHomeBtnAction(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Home");
		alert.setHeaderText("Are you sure you want to leave?");
		alert.setContentText("Current creation will not be saved.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			try {
				// Load root layout from fxml file.
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("mainMenu.fxml"));
				Pane rootLayout = loader.load();
				homeBtn.getScene().setRoot(rootLayout);

			} catch (IOException e) {
				e.printStackTrace();
			}
		} 


	}

	@FXML
	private void handleNextBtnAction(ActionEvent event) {
		try {	
			getSelectedImages();
			CreateCreationController controller = new CreateCreationController(_term, 10);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("createCreation.fxml"));
			loader.setController(controller);			
			nextBtn.getScene().setRoot(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getSelectedImages() {
		int i = 0;
		String filename;
		for(Field f: this.getClass().getFields()) {
			if (f.getType().equals(CheckBox.class)) {
				try {
					if (f.getName().equals("selectAllCB")) {
						filename = _term+i+".jpg";
					} else {
						filename = _term+"0"+i+".jpg";
					}
					ImageView image;

					image = (ImageView) f.get(this);

					image.setImage(new Image(filename));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	@FXML
	private void handleBackBtnAction(ActionEvent event) {
		try {	
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("audioView.fxml"));
			backBtn.getScene().setRoot(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

}
