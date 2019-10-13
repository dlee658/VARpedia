package application;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Contains a list of the current creations created for the view list
 * @author student
 *
 */
public class CreationList {
	private List<String> _creationList = new ArrayList<String>();

	public CreationList() {
		update();

	}

	public ObservableList<String> getCList() {
		ObservableList<String> content = FXCollections.observableArrayList(_creationList);
		return content;
	}

	/**
	 * Gets the latest list of creation files in the system and stores it in the creation list
	 */
	public void update() {
		_creationList.clear();
		String cmd = "ls Creations | sort";

		try {
			ProcessBuilder builder = new ProcessBuilder("bash","-c",cmd);
			Process process = builder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered =new BufferedReader(new InputStreamReader(stdout));

			String line = null;
			while ((line = stdoutBuffered.readLine()) != null ) {
				_creationList.add(line.substring(0, line.length()-4));
			}
		}
		catch(Exception e) {

		}
	}
}
