package controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
	private CheckBox selectAllCB;
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
		//retrieveImages(_term,10);
		displayImages();


	}

	private void displayImages() {
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
					String cwd = System.getProperty("user.dir");
					Image im = new Image("file:" + cwd + "/Images/" + filename);
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
		if (selectAllCB.isSelected()) {
			for(Field f: this.getClass().getFields()) {
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

	private static String getAPIKey(String key) throws Exception {
		String config = System.getProperty("user.dir") 
				+ System.getProperty("file.separator")+ "flickr-api-keys.txt"; 

		File file = new File(config); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 

		String line;
		while ( (line = br.readLine()) != null ) {
			if (line.trim().startsWith(key)) {
				br.close();
				return line.substring(line.indexOf("=")+1).trim();
			}
		}
		br.close();
		throw new RuntimeException("Couldn't find " + key +" in config file "+file.getName());
	}


	private void retrieveImages(String term, int numOfImages) {
		try {
			String apiKey = getAPIKey("apiKey");
			String sharedSecret = getAPIKey("sharedSecret");

			Flickr flickr = new Flickr(apiKey,sharedSecret, new REST());

			int page = 0;

			PhotosInterface photos = flickr.getPhotosInterface();
			SearchParameters params = new SearchParameters();
			params.setSort(SearchParameters.RELEVANCE);
			params.setMedia("photos"); 
			params.setText(term);

			PhotoList<Photo> results = photos.search(params,numOfImages, page);
			int i = 1;


			for (Photo photo: results) {
				try {
					BufferedImage image = photos.getImage(photo,Size.LARGE);
					String filename;	
					if (i == 10) {
						filename = term+i+".jpg";
					} else {
						filename = term+"0"+i+".jpg";
					}

					File outputfile = new File(filename);
					ImageIO.write(image, "jpg", outputfile);
				} catch (FlickrException fe) {
					System.err.println("Ignoring image " +photo.getId() +": "+ fe.getMessage());
				}
				i++;
			}

			nextBtn.setDisable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
