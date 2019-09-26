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
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class Create {

	private Tab createTab;
	private String term;
//	private int sentenceCount;
	private View _view;
	private int numberTxt = 0;

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
		Label msg = new Label("Select parts of the text:");
		//TextField sentenceField = new TextField(); 
		
		
		
		//sentenceField.setMaxWidth(50);
		
		
		Button previewBtn = new Button("Preview");
		Button saveBtn = new Button("Save");
		Button nextBtn = new Button("Next");
		HBox sentenceHB = new HBox(5,msg,previewBtn,saveBtn,nextBtn);
		sentenceHB.setPadding(new Insets(10));
		sentenceHB.setDisable(true);

		createPane.setTop(searchHB);
		createPane.setCenter(searchResult);
		createPane.setBottom(sentenceHB);

		searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sentenceHB.getChildren().clear();
				sentenceHB.getChildren().addAll(msg,previewBtn, saveBtn,nextBtn);
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
							//	int count = 1;
					//			searchResult.setText(count + ". " + line);
								while ((line = reader.readLine()) != null ) {
					//				count++;
									searchResult.appendText(line);
		//							sentenceCount = count;
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
		//button for next
		nextBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if(numberTxt>0) {
				createTab.setContent(creationPane());			
				}
				else {
					Label warningMsg = new Label("Save something, try again");
					sentenceHB.getChildren().clear();
					sentenceHB.getChildren().addAll(msg,previewBtn,saveBtn,nextBtn,warningMsg);					
				}
			
			}
			});
		//button for Preview
		previewBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String selectedPart = searchResult.getSelectedText();		
				if (selectedPart.length() == 0) {
					Label warningMsg = new Label("Select something, try again");
					sentenceHB.getChildren().clear();
					sentenceHB.getChildren().addAll(msg,previewBtn,saveBtn,nextBtn,warningMsg);								
				}
				else if (selectedPart.length() > 40) {
					Label warningMsg = new Label("Too long, try again");
					sentenceHB.getChildren().clear();
					sentenceHB.getChildren().addAll(msg,previewBtn,saveBtn,nextBtn,warningMsg);							
				}
				else {
		//preview the selectedpart
					
					
					ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "echo "+selectedPart+ " | festival --tts");
                    try {
						pb.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					sentenceHB.getChildren().clear();
					sentenceHB.getChildren().addAll(msg,previewBtn,saveBtn,nextBtn);		
				}
			
			}
			});
		//change to save
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String selectedPart = searchResult.getSelectedText();
				if (selectedPart == null) {
					Label warningMsg = new Label("Select something, try again");
					sentenceHB.getChildren().clear();
					sentenceHB.getChildren().addAll(msg,previewBtn,saveBtn,nextBtn,warningMsg);					
				} 
				else if (selectedPart.length() == 0) {
					Label warningMsg = new Label("Select something, try again");
					sentenceHB.getChildren().clear();
					sentenceHB.getChildren().addAll(msg,previewBtn,saveBtn,nextBtn,warningMsg);								
				}
				else if (selectedPart.length() > 40) {
					Label warningMsg = new Label("Too long, try again");
					sentenceHB.getChildren().clear();
					sentenceHB.getChildren().addAll(msg,previewBtn,saveBtn,nextBtn,warningMsg);							
				}
				else {
					numberTxt = numberTxt +1;
					createText(selectedPart);
					sentenceHB.getChildren().clear();
					sentenceHB.getChildren().addAll(msg,previewBtn,saveBtn,nextBtn);			

				}
				/*int numSentence = Integer.parseInt(sentenceField.getText());
				if (numSentence > 0 && numSentence <= sentenceCount) {
					createText(numSentence);
					createTab.setContent(creationPane());
				} else {
					Label warningMsg = new Label("Invalid number,try again");
					sentenceHB.getChildren().clear();
					sentenceHB.getChildren().addAll(msg,previewBtn,warningMsg);}*/
				
			}
		});

		return createPane;

	}
	



	/*
	 * protected void createText(int numSentence) { String cmd = "echo `head -" +
	 * numSentence + " \"Audio" + File.separatorChar + "audio_text.txt\"`";
	 * ProcessBuilder pb = new ProcessBuilder("bash","-c",cmd);
	 * pb.redirectOutput(new File("Audio" + File.separatorChar + term + ".txt"));
	 * try { Process process = pb.start(); process.waitFor();
	 * 
	 * } catch(Exception e) {
	 * 
	 * }
	 * 
	 * }
	 */

	protected void createText(String selectedText) {
		String cmd = "echo " + selectedText;
		ProcessBuilder pb = new ProcessBuilder("bash","-c",cmd);
		pb.redirectOutput(new File("Audio" + File.separatorChar + term +numberTxt+ ".txt"));
		
		audioCreation();
		
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

	
	//another method is required to combine all the audios
	
	
	//this needs to be implemented!! for the change synthtic speech setting with festival
	protected void audioCreation() {

		String audio = "\"Audio" + File.separatorChar + term +numberTxt+ ".wav\"";
		String text = "\"Audio" + File.separatorChar + term + numberTxt+".txt\"";


		ExecutorService createWorker = Executors.newSingleThreadExecutor(); 
		Task<File> task = new Task<File>() {
			@Override
			protected File call() throws Exception {
				try {
					String cmd = "text2wave -o " + audio + " " + text;
					ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
					Process audioProcess = pb.start();
					audioProcess.waitFor();

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

				//createTab.setContent(defaultPane());
			}	
		});

	}
	
	//***************
	protected void createCreation(String name) {

		String audio = "\"Audio" + File.separatorChar + term +numberTxt+ ".wav\"";
		String text = "\"Audio" + File.separatorChar + term + numberTxt+".txt\"";


		ExecutorService createWorker = Executors.newSingleThreadExecutor(); 
		Task<File> task = new Task<File>() {
			@Override
			protected File call() throws Exception {
				try {
					String cmd = "text2wave -o " + audio + " " + text;
					ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
					Process audioProcess = pb.start();
					audioProcess.waitFor();

					String video = "\"Video" + File.separatorChar + term + ".mp4\"";
					cmd = "ffmpeg -f lavfi -i color=c=blue:s=320x240:d=5 -t `soxi -D " + audio + "` -vf "
							+ "\"drawtext=FreeSerif.ttf:fontsize=30: fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text=" 
							+ term + "\" -y " + video;
					pb = new ProcessBuilder("bash", "-c", cmd);
					Process videoProcess = pb.start();
					videoProcess.waitFor();

					String creation = "\"Creations" + File.separatorChar + name + ".mp4\"";
					cmd = "ffmpeg -i " + video + " -i " + audio + " -c:v copy -c:a aac -strict experimental "+ creation;
					pb = new ProcessBuilder("bash", "-c", cmd);
					Process creationProcess = pb.start();
					creationProcess.waitFor();

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
//????************************
	
	
	
	
	
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


