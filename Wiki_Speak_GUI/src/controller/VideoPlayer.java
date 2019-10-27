package controller;

import java.io.File;
import helper.SceneChanger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.scene.media.MediaPlayer.Status;

/** Class for the video player that plays a selected creation
 * Code Credits: https://docs.oracle.com/javase/8/javafx/media-tutorial/playercontrol.htm
 */
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
	private Label creationLabel;

	@FXML
	private Label timeLabel;

	@FXML
	private Label volumeLabel;

	@FXML
	private Slider volumeSlider;

	private MediaPlayer mp;

	private Duration duration;


	public VideoPlayer(File file) {
		_file = file;
	}

	@FXML
	public void initialize() {
		Media video = new Media(_file.toURI().toString());
		mp = new MediaPlayer(video);
		creationLabel.setText(_file.getName().substring(0, _file.getName().length()-4));
		mp.setAutoPlay(true);
		mediaPlayer.setMediaPlayer(mp);
		setUpMediaBar();

	}

	/**display volume and time to user*/
	private void setUpMediaBar() {
		mp.currentTimeProperty().addListener(new InvalidationListener() 
		{
			public void invalidated(Observable ov) {
				updateValues();
			}
		});

		mp.setOnReady(new Runnable() {
			public void run() {
				duration = mp.getMedia().getDuration();
				updateValues();
			}
		});


		mp.setOnEndOfMedia(new Runnable() {
			public void run() {
				playBtn.setText("▷");
				mp.seek(mp.getStartTime());		
				mp.pause();
			}
		});

		//time
		timeSlider.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (timeSlider.isValueChanging()) {
					// multiply duration by percentage calculated by slider position
					mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
				}
			}
		});

		//volume
		volumeSlider.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (volumeSlider.isValueChanging()) {
					mp.setVolume(volumeSlider.getValue() / 100.0);
				}
			}
		});

	}

	/**update values for volume and time*/
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

	/**for time*/
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
	/**go to previous page*/
	@FXML
	private void handleBackBtnAction(ActionEvent event) {
			mp.pause();
			// returns to view page
			SceneChanger.changeScene(null, "viewView.fxml", backBtn);
	}
	/**play button that allows stop and play*/
	
	@FXML
	private void handlePlayBtnAction(ActionEvent event) {
		Status status = mp.getStatus();

		if ( status == Status.PAUSED || status == Status.READY || status == Status.STOPPED)	{
			mp.play();
			playBtn.setText("||");
		} else {
			mp.pause();
			playBtn.setText("▷");
		}
	}

}
