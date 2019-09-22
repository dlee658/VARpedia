package application;

import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class Home {
	
	private Tab homeTab;

	public Home() {
		homeTab = new Tab("Home");
		homeTab.setContent(defaultPane());
	}
		
	public Pane defaultPane() {
		Text welcomeMsg = new Text("Welcome to the Wiki-Speak Authoring Tool\n");
		welcomeMsg.setFont(Font.font(30));
		Text instructMsg = new Text("Choose the Create Creation tab to create a creation "
				+ "or the View Creation tab to view/play/delete your creations :)");
		TextFlow homePane = new TextFlow(welcomeMsg,instructMsg);
		homePane.setLineSpacing(10);
		homePane.setTextAlignment(TextAlignment.CENTER);
		homePane.setPadding(new Insets(20));
		
		return homePane;
	}

	public Tab getTab() {
		return homeTab;
	}
}
