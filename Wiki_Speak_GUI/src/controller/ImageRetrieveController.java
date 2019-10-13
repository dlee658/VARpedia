package controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ImageRetrieveController {
	@FXML 
	private Button homeBtn;

	@FXML 
	private Button retrieveBtn;

	@FXML 
	private Button nextBtn;

	@FXML 
	private Button backBtn;

	@FXML 
	private ProgressIndicator imageIndicator;

	@FXML
	private HBox HB1;

	@FXML
	private HBox HB2;
	
	@FXML
	private HBox HB3;

	@FXML 
	private ChoiceBox<Integer> imageCB;

	private int numOfImages;

	private String term;

	@FXML
	public void initialize() {
		imageCB.setValue(1);
		imageCB.getItems().addAll(1,2,3,4,5,6,7,8,9,10);
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("term.txt"));
			term = reader.readLine();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void handleHomeBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("mainMenu.fxml"));
			homeBtn.getScene().setRoot(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleRetrieveBtnAction(ActionEvent event) {
		HB1.getChildren().clear();
		HB2.getChildren().clear();
		HB3.getChildren().clear();
		imageIndicator.setVisible(true);
		numOfImages = imageCB.getValue();
		retrieveImages(term,numOfImages);
	}


	@FXML
	private void handleNextBtnAction(ActionEvent event) {
		try {	
			CreateCreationController controller = new CreateCreationController(numOfImages);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("createCreation.fxml"));
			loader.setController(controller);			
			nextBtn.getScene().setRoot(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
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
					Image inputImage;
					
					if (i<=4) {
						try {
							String cwd = System.getProperty("user.dir");
							FileInputStream input;
							//display the image
							input = new FileInputStream(cwd + File.separatorChar + filename);
							inputImage = new Image(input,0,HB1.getHeight(),true,true);
							ImageView imageView = new ImageView();
							imageView.setImage(inputImage);
							HB1.getChildren().add(imageView);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
						
					} else if (i > 4 && i <= 8) {
						try {
							String cwd = System.getProperty("user.dir");
							FileInputStream input;
							//display the image
							input = new FileInputStream(cwd + File.separatorChar + filename);
							inputImage = new Image(input,0,HB2.getHeight(),true,true);
							ImageView imageView = new ImageView();
							imageView.setImage(inputImage);
							HB2.getChildren().add(imageView);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
					} else {
						try {
							String cwd = System.getProperty("user.dir");
							FileInputStream input;
							//display the image
							input = new FileInputStream(cwd + File.separatorChar + filename);
							inputImage = new Image(input,0,HB3.getHeight(),true,true);
							ImageView imageView = new ImageView();
							imageView.setImage(inputImage);
							HB3.getChildren().add(imageView);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					i++;
				} catch (FlickrException fe) {
					System.err.println("Ignoring image " +photo.getId() +": "+ fe.getMessage());
				}
			}
			imageIndicator.setVisible(false);
			nextBtn.setDisable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
