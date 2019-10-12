package controller;

import java.io.File;
import java.io.IOException;

import application.Main;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.scene.media.MediaPlayer.Status;

public class VideoPlayer{

	private File _file;

	@FXML
	private Button playBtn;

	@FXML
	private Button backBtn;

	@FXML
	private MediaView mediaPlayer;

	@FXML
	private Slider timeSlider;

	@FXML
	private Label timeLabel;

	@FXML
	private Label volumeLabel;

	@FXML
	private Slider volumeSlider;

	private MediaPlayer mp;
	private final boolean repeat = false;
	private boolean stopRequested = false;
	private boolean atEndOfMedia = false;
	private Duration duration;


	public VideoPlayer(File file) {
		_file = file;
	}
	
	@FXML
	public void initialize() {
		Media video = new Media(_file.toURI().toString());
		mp = new MediaPlayer(video);
		mp.setAutoPlay(true);
		mediaPlayer.setMediaPlayer(mp);
		setUpMediaBar();

	}

	private void setUpMediaBar() {
		mp.currentTimeProperty().addListener(new InvalidationListener() 
		{
			public void invalidated(Observable ov) {
				updateValues();
			}
		});

		mp.setOnPlaying(new Runnable() {
			public void run() {
				if (stopRequested) {
					mp.pause();
					stopRequested = false;
				} else {
					playBtn.setText("||");
				}
			}
		});

		mp.setOnPaused(new Runnable() {
			public void run() {
				playBtn.setText(">");
			}
		});

		mp.setOnReady(new Runnable() {
			public void run() {
				duration = mp.getMedia().getDuration();
				updateValues();
			}
		});

		mp.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
		
		mp.setOnEndOfMedia(new Runnable() {
			public void run() {
				if (!repeat) {
					playBtn.setText(">");
					stopRequested = true;
					atEndOfMedia = true;
				}
			}
		});

		timeSlider.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (timeSlider.isValueChanging()) {
					// multiply duration by percentage calculated by slider position
					mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
				}
			}
		});

		volumeSlider.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (volumeSlider.isValueChanging()) {
					mp.setVolume(volumeSlider.getValue() / 100.0);
				}
			}
		});

	}

	protected void updateValues() {
		if (timeLabel != null && timeSlider != null && volumeSlider != null) {
			Platform.runLater(new Runnable() {
				public void run() {
					Duration currentTime = mp.getCurrentTime();
					timeLabel.setText(formatTime(currentTime, duration));
					timeSlider.setDisable(duration.isUnknown());
					if (!timeSlider.isDisabled() 
							&& duration.greaterThan(Duration.ZERO) 
							&& !timeSlider.isValueChanging()) {
						timeSlider.setValue(currentTime.divide(duration).toMillis()
								* 100.0);
					}
					if (!volumeSlider.isValueChanging()) {
						volumeSlider.setValue((int)Math.round(mp.getVolume() 
								* 100));
					}
				}
			});
		}
	}

	private static String formatTime(Duration elapsed, Duration duration) {
		int intElapsed = (int)Math.floor(elapsed.toSeconds());
		int elapsedHours = intElapsed / (60 * 60);
		if (elapsedHours > 0) {
			intElapsed -= elapsedHours * 60 * 60;
		}
		int elapsedMinutes = intElapsed / 60;
		int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 
				- elapsedMinutes * 60;

		if (duration.greaterThan(Duration.ZERO)) {
			int intDuration = (int)Math.floor(duration.toSeconds());
			int durationHours = intDuration / (60 * 60);
			if (durationHours > 0) {
				intDuration -= durationHours * 60 * 60;
			}
			int durationMinutes = intDuration / 60;
			int durationSeconds = intDuration - durationHours * 60 * 60 - 
					durationMinutes * 60;
			if (durationHours > 0) {
				return String.format("%d:%02d:%02d/%d:%02d:%02d", 
						elapsedHours, elapsedMinutes, elapsedSeconds,
						durationHours, durationMinutes, durationSeconds);
			} else {
				return String.format("%02d:%02d/%02d:%02d",
						elapsedMinutes, elapsedSeconds,durationMinutes, 
						durationSeconds);
			}
		} else {
			if (elapsedHours > 0) {
				return String.format("%d:%02d:%02d", elapsedHours, 
						elapsedMinutes, elapsedSeconds);
			} else {
				return String.format("%02d:%02d",elapsedMinutes, 
						elapsedSeconds);
			}
		}
	}

	@FXML
	private void handleBackBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
			mp.pause();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("viewView.fxml"));
			backBtn.getScene().setRoot(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handlePlayBtnAction(ActionEvent event) {
		Status status = mp.getStatus();

		if ( status == Status.PAUSED
				|| status == Status.READY
				|| status == Status.STOPPED)
		{
			// rewind the movie if we're sitting at the end
			if (atEndOfMedia) {
				mp.seek(mp.getStartTime());
				atEndOfMedia = false;
			}
			mp.play();
		} else {
			mp.pause();
		}
	}



}
