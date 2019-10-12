package application;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.flickr4java.flickr.*;
import com.flickr4java.flickr.photos.*;

public class VideoCreation {

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

			Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());

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
					i++;
				} catch (FlickrException fe) {
					System.err.println("Ignoring image " +photo.getId() +": "+ fe.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createVideo(String term,int numOfImages ,String name) {
		String video = "\"Video" + File.separatorChar + term + ".mp4\"";
		String audio = "\"Audio" + File.separatorChar + term + ".wav\"";
		String creation = "\"Creations" + File.separatorChar + name + ".mp4\"";

		try {
			retrieveImages(term, numOfImages);
			
//			String cmd = "cat *.jpg | ffmpeg -framerate " + numOfImages + "/`soxi -D " + audio + "` -i - -c:v libx264 -pix_fmt yuv420p -vf \"scale=640:480:force_original_aspect_ratio=decrease,"
//					+ "pad=640:480:(ow-iw)/2:(oh-ih)/2,drawtext=FreeSerif.ttf:fontsize=50: fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text=" + term + "\" -r 25 -y " + video 
//					+ " &> status.txt ;ffmpeg -i " + video + " -i " + audio + " -c:v copy -c:a aac -strict experimental "+ creation + " &> status2.txt";
				
			String cmd = "cat *.jpg | ffmpeg -f image2pipe -framerate " + numOfImages + "/`soxi -D " + audio + "` -i - -c:v libx264 -pix_fmt yuv420p -vf \"scale=640:480:force_original_aspect_ratio=decrease,"
					+ "pad=640:480:(ow-iw)/2:(oh-ih)/2,drawtext=FreeSerif.ttf:fontsize=50: fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text=" + term + "\" -r 25 -max_muxing_queue_size 1024 -y " + creation
					+ " &> status.txt"; 
			
			//cat *.jpg | ffmpeg -f image2pipe -framerate $framerate -i - -i audio.wav -c:v libx264 -pix_fmt yuv420p -vf "scale=width:height" -r 25 -max_muxing_queue_size 1024 -y out.mp4 
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
			Process creationProcess = pb.start();

			creationProcess.waitFor();
			

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
