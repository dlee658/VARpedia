package application;

import java.io.File;
import java.io.IOException;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class VideoPlayer {

	public void play(File file) {
		Stage videoPlayer = new Stage();
		videoPlayer.setTitle(file.getName());
		
		BorderPane mediaPane = new BorderPane();		
		Media video = new Media(file.toURI().toString());
		MediaPlayer player = new MediaPlayer(video);
		player.setAutoPlay(true);
		MediaView mediaView = new MediaView(player);
		//mediaPane.setCenter(mediaView);
		
		Button playBtn = new Button("Play/Pause");
		Button forwardBtn = new Button(">>");
		Button backwardBtn = new Button("<<");
		HBox controls = new HBox(10);
		controls.getChildren().addAll(backwardBtn,playBtn,forwardBtn);
		VBox vb = new VBox(10);
		vb.getChildren().addAll(mediaView,controls);
		//mediaPane.setBottom(controls);
		
		Button backBtn = new Button("Back");
		mediaPane.setTop(backBtn);
		mediaPane.setCenter(vb);
		mediaPane.setPadding(new Insets(10));
		
		videoPlayer.setScene(new Scene(mediaPane));
		videoPlayer.show();
		//player.setOnEndOfMedia(new EventHandler<>());
	}

}
