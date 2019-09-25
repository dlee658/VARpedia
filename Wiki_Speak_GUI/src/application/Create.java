package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Create {

	private Tab createTab;
	private String term;
	private int sentenceCount;
	private View _view;
	protected int numOfImages;

	public Create(View view) {
		createTab = new Tab("Create Creation");
		createTab.setContent(defaultPane());
		_view = view;
	}

	public Pane defaultPane() {
		BorderPane createPane = new BorderPane();
		TextField searchField = new TextField(); 
		searchField.setPromptText("Enter search term:");
		Button searchBtn = new Button("Search");
		HBox searchHB = new HBox(5,searchField,searchBtn); 
		searchHB.setPadding(new Insets(10));
		TextArea searchResult = new TextArea(); 
		//searchResult.setEditable(false);
		Label msg = new Label("Enter number of sentences you'll like in the creation:");
		TextField sentenceField = new TextField(); 
		sentenceField.setMaxWidth(50);
		Button enterBtn = new Button("Enter");
		ChoiceBox cb = new ChoiceBox();
		for (int i = 1;i<=10;i++) {
			cb.getItems().add(i);
		}
		
		
		HBox sentenceHB = new HBox(5,msg,sentenceField,enterBtn);
		sentenceHB.setPadding(new Insets(10));
		sentenceHB.setDisable(true);
		
		VBox vb = new VBox(sentenceHB,cb);

		createPane.setTop(searchHB);
		createPane.setCenter(searchResult);
		createPane.setBottom(vb);

		searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sentenceHB.getChildren().clear();
				sentenceHB.getChildren().addAll(msg,sentenceField,enterBtn);
				term = searchField.getText().trim();

				if (term.isEmpty()) {
					sentenceHB.setDisable(true);
					searchResult.clear();
					return;
				}
				
				searchBtn.setDisable(true);
				getSearchResult();

			}

			private void getSearchResult() {
				String command = "wikit " + term + " | sed 's/\\. /&\\n/g'";
				ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
				File text = new File("Audio" + File.separatorChar + "audio_text.txt");
				pb.redirectOutput(text);

				ExecutorService worker = Executors.newSingleThreadExecutor(); 
				Task<Boolean> task = new Task<Boolean>() {
					@Override
					protected Boolean call() throws Exception {
						try {
							Process searchProcess = pb.start(); 
							searchProcess.waitFor();
							
							
							
						}catch (IOException e) {
							e.printStackTrace();
						}
						return true;
					}
				};

				worker.submit(task);
				task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent event) {
						BufferedReader reader;
						try {
							reader = new BufferedReader(new FileReader(text));
							String line = reader.readLine().trim();
							if (line.equals(term + " not found :^(")) {
								searchResult.setText(term + " not found, please try again");
								sentenceHB.setDisable(true);

							} else {
								int count = 1;
								searchResult.setText(count + ". " + line);
								while ((line = reader.readLine()) != null ) {
									count++;
									searchResult.appendText("\n" + count  + ". " + line);
									sentenceCount = count;
									sentenceHB.setDisable(false);
								}
								reader.close();
							}
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

						searchBtn.setDisable(false);
					}
				});
			}
		});

		enterBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int numSentence = Integer.parseInt(sentenceField.getText());
				if (numSentence > 0 && numSentence <= sentenceCount) {
					createText(numSentence);
					numOfImages = Integer.parseUnsignedInt(cb.getValue().toString());
					createTab.setContent(creationPane());
				} else {
					Label warningMsg = new Label("Invalid number,try again");
					sentenceHB.getChildren().clear();
					sentenceHB.getChildren().addAll(msg,sentenceField,enterBtn,warningMsg);
				}
			}
		});

		return createPane;

	}


	protected void createText(int numSentence) {
		String cmd = "echo `head -" + numSentence + " \"Audio" + File.separatorChar + "audio_text.txt\"`";
		ProcessBuilder pb = new ProcessBuilder("bash","-c",cmd);
		pb.redirectOutput(new File("Audio" + File.separatorChar + term + ".txt"));
		try {
			Process process = pb.start();
			process.waitFor();

		}
		catch(Exception e) {

		}

	}

	public Pane creationPane() {
		BorderPane creationPane = new BorderPane();
		TextField nameField = new TextField(); 
		Label nameLabel = new Label("Enter name of your new creation:");
		Button createBtn = new Button("Create!");
		HBox createHB = new HBox(5,nameLabel,nameField,createBtn); 
		createHB.setPadding(new Insets(10));
		Button backBtn = new Button("Back");
		HBox backHB = new HBox(backBtn);
		backHB.setPadding(new Insets(10));

		creationPane.setTop(createHB);
		creationPane.setBottom(backHB);

		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				createTab.setContent(defaultPane());
			}
		});

		createBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String name = nameField.getText();
				if(isValidName(name)) {
					createCreation(name);
				} else { 
					Label msg = new Label("Existing creation called '" + name + "' , Enter another name");
					creationPane.setCenter(msg);
				}


			}
		});

		return creationPane;
	}

	protected void createCreation(String name) {

		String audio = "\"Audio" + File.separatorChar + term + ".wav\"";
		String text = "\"Audio" + File.separatorChar + term + ".txt\"";


		ExecutorService createWorker = Executors.newSingleThreadExecutor(); 
		Task<File> task = new Task<File>() {
			@Override
			protected File call() throws Exception {
				try {
					String cmd = "text2wave -o " + audio + " " + text;
					ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
					Process audioProcess = pb.start();
					audioProcess.waitFor();

					VideoCreation vc = new VideoCreation();
					vc.createVideo(term, numOfImages,name);
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;	
			}
		};

		createWorker.submit(task);
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Creation complete");
				alert.setHeaderText(null);
				alert.setContentText("Creation '" +name+ "' created");
				alert.show();
				_view.update(name);
				createTab.setContent(defaultPane());
			}	
		});
	}

	protected boolean isValidName(String name) {
		File file = new File("Creations" + File.separatorChar + name + ".mp4");
		if(file.exists()) {
			return false;
		}
		return true;
	}


	public Tab getTab() {
		return createTab;
	}
}


