package application;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
		videoPlayerWindow.setResizable(false);

		BorderPane mediaPane = new BorderPane();		
		Media video = new Media(file.toURI().toString());
		MediaPlayer player = new MediaPlayer(video);
		player.setAutoPlay(true);
		MediaView mediaView = new MediaView(player);

		Button playBtn = new Button("Play/Pause");
		Button forwardBtn = new Button(">>");
		Button backwardBtn = new Button("<<");
		HBox controls = new HBox(10);
		controls.setAlignment(Pos.CENTER);
		controls.getChildren().addAll(backwardBtn,playBtn,forwardBtn);
		controls.setStyle("-fx-background-color:#433f3e");
		controls.setPadding(new Insets(5));

		VBox vb = new VBox();
		vb.getChildren().addAll(mediaView,controls);
		mediaPane.setCenter(vb);

		Scene scene = new Scene(mediaPane,640,515);	
		mediaView.fitWidthProperty().bind(scene.widthProperty()); 
		videoPlayerWindow.setScene(scene);
		videoPlayerWindow.show();

		videoPlayerWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				player.stop();
			}
		});

		player.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				videoPlayerWindow.close();

			}

		});


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
