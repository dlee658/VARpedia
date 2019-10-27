package helper;

import java.io.File;

public class VideoCreation {

	/**
	 * This method combines the audio with the downloaded images to create the creation.
	 * It also creates the text less creation for the quiz
	 */
	public void createVideo(String term,int numOfImages ,String name) {
		String video = "\"Video" + File.separatorChar + name + ".mp4\"";
		String audio = "\"Audio" + File.separatorChar + term + ".wav\"";
		String creation = "\"Creations" + File.separatorChar + name + ".mp4\"";

		//Create textless creation
		String cmd = "cat *.jpg | ffmpeg -f image2pipe -framerate " + numOfImages + "/`soxi -D " + audio + "` -i - -i "+ audio + " -c:v libx264 -pix_fmt yuv420p -vf \"scale=640:480:force_original_aspect_ratio=decrease,"
				+ "pad=640:480:(ow-iw)/2:(oh-ih)/2\" -r 25 -max_muxing_queue_size 1024 -y " + video + " &> status.txt;"; 

		//Create creation
		String cmd2 = "ffmpeg -i "+ video + " -c:v libx264 -pix_fmt yuv420p -vf \"drawtext=FreeSerif.ttf:fontsize=60: fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:borderw=3:text=" + term + "\" -r 25 -max_muxing_queue_size 1024 -y " + creation  +" &> status2.txt"; 

		BashCommand.runCommand(cmd + cmd2);			
	}
}
