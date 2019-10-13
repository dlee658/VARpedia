package application;

import java.io.File;
import java.io.IOException;

public class VideoCreation {

	/**
	 * This method combines the audio with the downloaded images to create the creation.
	 * It also creates the text less creation for the quiz
	 */
	public void createVideo(String term,int numOfImages ,String name) {
		String video = "\"Video" + File.separatorChar + name + ".mp4\"";
		String audio = "\"Audio" + File.separatorChar + term + ".wav\"";
		String creation = "\"Creations" + File.separatorChar + name + ".mp4\"";

		try {	
			//Create creation
			String cmd = "cat *.jpg | ffmpeg -f image2pipe -framerate " + numOfImages + "/`soxi -D " + audio + "` -i - -i "+ audio + " -c:v libx264 -pix_fmt yuv420p -vf \"scale=640:480:force_original_aspect_ratio=decrease,"
					+ "pad=640:480:(ow-iw)/2:(oh-ih)/2,drawtext=FreeSerif.ttf:fontsize=50: fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text=" + term + "\" -r 25 -max_muxing_queue_size 1024 -y " + creation + ";"; 
			
			//Create textless creation
			String cmd2 = "cat *.jpg | ffmpeg -f image2pipe -framerate " + numOfImages + "/`soxi -D " + audio + "` -i - -i "+ audio + " -c:v libx264 -pix_fmt yuv420p -vf \"scale=640:480:force_original_aspect_ratio=decrease,"
					+ "pad=640:480:(ow-iw)/2:(oh-ih)/2\" -r 25 -max_muxing_queue_size 1024 -y " + video; 
			
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd + cmd2);
			Process creationProcess = pb.start();

			creationProcess.waitFor();
			

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
