package application;

import java.io.File;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class VideoPlayer {

	public void play(File file) {
		Stage videoPlayerWindow = new Stage();
		videoPlayerWindow.setTitle(file.getName());
		
		BorderPane mediaPane = new BorderPane();		
		Media video = new Media(file.toURI().toString());
		MediaPlayer player = new MediaPlayer(video);
		player.setAutoPlay(true);
		MediaView mediaView = new MediaView(player);
		//mediaView.setPreserveRatio(true);
			
		//mediaPane.setCenter(mediaView);
		
	
		Button playBtn = new Button("Play/Pause");
		Button forwardBtn = new Button(">>");
		Button backwardBtn = new Button("<<");
		HBox controls = new HBox(10);
		controls.setAlignment(Pos.CENTER);
		controls.getChildren().addAll(backwardBtn,playBtn,forwardBtn);
		VBox vb = new VBox(10);
		vb.getChildren().addAll(mediaView,controls);
		//mediaPane.setBottom(controls);
		
		//Button backBtn = new Button("Back");
		//mediaPane.setTop(backBtn);
	mediaPane.setCenter(vb);
		//mediaPane.setPadding(new Insets(10));
		
		
		Scene scene = new Scene(mediaPane,640,500);	
		mediaView.fitWidthProperty().bind(scene.widthProperty()); 
		mediaView.fitHeightProperty().bind(scene.heightProperty());
//		videoPlayer.setScene(new Scene(640,480));
		videoPlayerWindow.setScene(scene);
		videoPlayerWindow.show();
		
		

		videoPlayerWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				player.stop();
			}
		});
		
//		player.setOnEndOfMedia(new Runnable() {
//			@Override
//			public void run() {
//				videoPlayerWindow.close();
//				
//			}
//			
//		});
		
		
		playBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (player.getStatus() == Status.PLAYING) {
					player.pause();
				} else {
					player.play();
				}
			}
		});

		
		forwardBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				player.seek( player.getCurrentTime().add( Duration.seconds(2)) );
			}
		});
		

		backwardBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				player.seek( player.getCurrentTime().add( Duration.seconds(-2)) );
			}
		});
		
	}

}
