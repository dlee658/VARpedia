package application;

import java.io.File;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class View {

	private ListView<String> viewListView;
	private Tab viewTab;
	private CreationList creationList;

	public View() {
		viewTab = new Tab("View Creations");
		creationList = new CreationList();
		viewTab.setContent(defaultPane());
	}

	public Pane defaultPane() {
		BorderPane viewPane = new BorderPane();
		Button deleteBtn = new Button("delete");
		Button playBtn = new Button("play");
		
		deleteBtn.setMaxWidth(Double.MAX_VALUE);
		playBtn.setMaxWidth(Double.MAX_VALUE);
		
		VBox vb = new VBox(10);
		vb.setPadding(new Insets(0,0,20,20));
		vb.getChildren().addAll(playBtn,deleteBtn);
		viewPane.setRight(vb);

		viewListView = new ListView<String>();
		viewListView.setItems(creationList.getCList()); 
		viewListView.setPlaceholder(new Label("No creations created"));
		viewPane.setPadding(new Insets(20));
		viewPane.setCenter(viewListView);

		deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				confirmDeleteCreation();
			}
		});

		playBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				playCreation();
			}

		});

		return viewPane;
	}

	public void confirmDeleteCreation() {
		String fileName = viewListView.getSelectionModel().getSelectedItem();
		if (fileName == null) {
			return;
		}

		ButtonType yesBtn = new ButtonType("yes");
		ButtonType noBtn = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);
		Alert deleteAlert = new Alert(AlertType.CONFIRMATION,"Are you sure you want to delete: " + fileName,yesBtn,noBtn);
		deleteAlert.setTitle("Confirm Deletion");
		deleteAlert.setHeaderText(null);

		Optional<ButtonType> btn = deleteAlert.showAndWait();

		if (btn.get() == yesBtn) {
			File file = new File("Creations" + File.separatorChar + fileName + ".mp4");
			file.delete();
			update(fileName);
			deleteAlert.close();
		} 
	}

	public void playCreation() {
		String fileName = viewListView.getSelectionModel().getSelectedItem();
		if (fileName == null) {
			return;
		}
		File file = new File("Creations" + File.separatorChar + fileName + ".mp4");

		VideoPlayer vp = new VideoPlayer();
		vp.play(file);
	}

	public Tab getTab() {
		return viewTab;
	}

	public void update(String fileName) {
		creationList.update();
		viewListView.setItems(creationList.getCList()); 

	}


}
