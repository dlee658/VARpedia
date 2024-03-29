package controller;

import java.lang.reflect.Field;
import java.util.Optional;
import helper.BashCommand;
import helper.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**This controller manages the image selection page which allows users to select photos for the creation*/

public class RetrieveImage {
	@FXML 
	private Button homeBtn;

	@FXML 
	private Button nextBtn;

	@FXML 
	private Button helpBtn;

	@FXML 
	private VBox helpBox;

	@FXML 
	private BorderPane imagePane;

	@FXML 
	private Label msg;

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

	private int _numOfImages;

	private boolean helpOn = false;

	public RetrieveImage(String term) {
		_term = term;
	}

	@FXML
	public void initialize() {
		displayImages();
		nextBtn.setDisable(false);
	}

	/**show 10 images to user with given searched term*/
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
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**Makes all the check boxes ticked*/
	@FXML
	private void handleSelectAllAction(ActionEvent event) {
		for(Field f: this.getClass().getDeclaredFields()) {
			if (f.getType().equals(CheckBox.class)) {
				try {
					CheckBox checkBox = (CheckBox) f.get(this);
					checkBox.setSelected(true);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**Toggles the help dialog*/
	@FXML
	private void handleHelpBtnAction(ActionEvent event) {
		helpOn  = !helpOn;
		if (helpOn) {
			imagePane.setDisable(true);
			helpBox.setVisible(true);
			helpBtn.setText("X");
		} else {
			imagePane.setDisable(false);
			helpBox.setVisible(false);
			helpBtn.setText("?");
		}
	}

	/**return to main page, ask user again for confirmation*/
	@FXML
	private void handleHomeBtnAction(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Home");
		alert.setHeaderText("Are you sure you want to leave?");
		alert.setContentText("Current creation will not be saved.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			SceneChanger.changeScene(null, "mainMenu.fxml", homeBtn);
		} 
	}

	/**go to next page which is asking user for the creation name*/
	@FXML
	private void handleNextBtnAction(ActionEvent event) {
		if (getSelectedImages()) {
			CreateCreationController controller = new CreateCreationController(_term, _numOfImages);
			SceneChanger.changeScene(controller, "createCreation.fxml", nextBtn);
		} else {
			msg.setText("Please select at least one image");
		}
	}

	/**Gets the images that the user selects*/
	private boolean getSelectedImages() {
		int i = 1;
		_numOfImages = 0;
		for(Field f: this.getClass().getDeclaredFields()) {
			if (f.getType().equals(CheckBox.class)) {
				try {
					CheckBox checkBox = (CheckBox) f.get(this);
					if (checkBox.isSelected() == true) {
						_numOfImages++;
					} else {
						String fileName;
						if (i == 10) {
							fileName = _term+i+".jpg";
						} else {
							fileName = _term+"0"+i+".jpg";
						}

						String cmd = "rm " + fileName;
						BashCommand.runCommand(cmd);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				i++;
			}
		}
		if (_numOfImages == 0) {
			return false;
		} 
		return true;
	}
}
