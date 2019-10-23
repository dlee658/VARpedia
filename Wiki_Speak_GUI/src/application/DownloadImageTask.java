package application;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.imageio.ImageIO;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;

import javafx.concurrent.Task;

public class DownloadImageTask extends Task<Object> {

	
	private String _term;


	public DownloadImageTask(String term) {
		_term = term;
	}
	@Override
	protected Object call() throws Exception {
		retrieveImages(_term, 10);

		return null;
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
