package controller;

import java.io.File;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class VideoPlayer{

	private File _file;
	
	@FXML
	private Button playBtn;
	
	@FXML
	private MediaView mediaPlayer;
	

	public VideoPlayer(File file) {
		_file = file;
	}
	
	@FXML
	public void initialize() {
		Media video = new Media(_file.toURI().toString());
		MediaPlayer player = new MediaPlayer(video);
		player.setAutoPlay(true);
		mediaPlayer.setMediaPlayer(player);
		
	}
	

}
