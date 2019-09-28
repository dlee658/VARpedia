package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
	//	private int sentenceCount;
	private View _view;
	private int numberTxt = 0;
	protected int numOfImages;
	protected String voice;
	
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
		searchResult.setWrapText(true);

		Label msg = new Label("Select parts of the text:");


		ChoiceBox<Integer> imageCB = new ChoiceBox<Integer>();
		imageCB.setValue(1);
		for (int i = 1;i<=10;i++) {
			imageCB.getItems().add(i);
		}
		
		ChoiceBox<String> voiceCB = new ChoiceBox<String>();
		voiceCB.setValue("voice_kal_diphone");
		voiceCB.getItems().addAll("voice_kal_diphone", "voice_akl_nz_jdt_diphone", "voice_akl_nz_cw_cg_cg");

		Button previewBtn = new Button("Preview");
		Button saveBtn = new Button("Save");
		Button nextBtn = new Button("Next");
		HBox sentenceHB = new HBox(5,msg,previewBtn,saveBtn,nextBtn);
		sentenceHB.setPadding(new Insets(10));
		sentenceHB.setDisable(true);
		VBox vb = new VBox(sentenceHB,imageCB, voiceCB);

		createPane.setTop(searchHB);
		createPane.setCenter(searchResult);
		createPane.setBottom(vb);

		searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				String command = "rm Audio/*.txt; rm  Audio/*.wav";
				ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);			
				try {
					Process searchProcess = pb.start(); 
					searchProcess.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				numberTxt = 0;
				sentenceHB.getChildren().clear();
				sentenceHB.getChildren().addAll(msg,previewBtn,saveBtn,nextBtn);
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

								searchResult.setText(line);
								while ((line = reader.readLine()) != null ) {
									searchResult.appendText(line);
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
					if(imageCB.getValue() != null) {
						numOfImages = Integer.parseUnsignedInt(imageCB.getValue().toString());
						createTab.setContent(creationPane());						
					}
					else {
						Label warningMsg = new Label("Choose number of images, try again");
						sentenceHB.getChildren().clear();
						sentenceHB.getChildren().addAll(msg,previewBtn,saveBtn,nextBtn,warningMsg);		
					}
					//nullpointerexception
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
				int wordCount = selectedPart.split("\\s+").length;
				if (selectedPart.isEmpty()) {
					Label warningMsg = new Label("Select something, try again");
					sentenceHB.getChildren().clear();
					sentenceHB.getChildren().addAll(msg,previewBtn,saveBtn,nextBtn,warningMsg);						
				}

				

				else if (wordCount > 20) { 
					Label warningMsg = new Label("Too long, try again");
					sentenceHB.getChildren().clear();
					sentenceHB.getChildren().addAll(msg,previewBtn,saveBtn,nextBtn,warningMsg);							
				}
				else {
					//preview the selected part
					ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "echo "+selectedPart+ " | festival --tts");
					try {
						pb.start();
					} catch (IOException e) {
						e.printStackTrace();
					}

						
				}
sentenceHB.getChildren().clear();
					sentenceHB.getChildren().addAll(msg,previewBtn,saveBtn,nextBtn);	
			}
		});


		//Save Audio files
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String selectedPart = searchResult.getSelectedText();
				int wordCount = selectedPart.split("\\s+").length;
				/*				
				ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "echo " + selectedPart + " | wc -w");
                try {
					pb.start();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	*/	

				if (selectedPart.isEmpty()) {
					Label warningMsg = new Label("Select something, try again");
					sentenceHB.getChildren().clear();
					sentenceHB.getChildren().addAll(msg,previewBtn,saveBtn,nextBtn,warningMsg);					
				} 
				else if (wordCount > 20) {
					Label warningMsg = new Label("Too long, try again");
					sentenceHB.getChildren().clear();
					sentenceHB.getChildren().addAll(msg,previewBtn,saveBtn,nextBtn,warningMsg);							
				}
				else {

						numberTxt = numberTxt +1;
						//ask user for the setting??
						if(voiceCB.getValue() != null) {

							voice = voiceCB.getValue().toString();
							createText(selectedPart);

							audioCreation();
						}					
						else {
							//set default voice as kal diphone
							voice = "voice_kal_diphone";
							createText(selectedPart);

							audioCreation();
						
					}


					sentenceHB.getChildren().clear();
					sentenceHB.getChildren().addAll(msg,previewBtn,saveBtn,nextBtn);			




				}
			}
		});

		return createPane;

	}

	protected void createText(String selectedText) {
		String cmd = "echo " + selectedText;
		ProcessBuilder pb = new ProcessBuilder("bash","-c",cmd);
		pb.redirectOutput(new File("Audio" + File.separatorChar + term +numberTxt+ ".txt"));

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
				
				
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Back");
				alert.setHeaderText("Are you sure you want to leave?");
				alert.setContentText("Current creation will not be saved.");


				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					createTab.setContent(defaultPane());
				} else {
				    // ... user chose CANCEL or closed the dialog
				}
				
				

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


	//this needs to be implemented!! for the change synthetic speech setting with festival
	protected void audioCreation() {

		String audio = "\"Audio" + File.separatorChar + term +numberTxt+ ".wav\"";
		String text = "\"Audio" + File.separatorChar + term + numberTxt+".txt\"";
		


		ExecutorService createWorker = Executors.newSingleThreadExecutor(); 
		Task<File> task = new Task<File>() {
			@Override
			protected File call() throws Exception {
				try {

					String voiceFile;
					String cmd;
				
					if(voice.equals("voice_kal_diphone")) {
						 voiceFile = "\"Voice" + File.separatorChar + "kal.scm\"";
				//	 cmd = "text2wave -o " + audio + " " + text + " -eval kal.scm";
					}
					else if(voice.equals("voice_akl_nz_jdt_diphone")) {
						 voiceFile = "\"Voice" + File.separatorChar + "jdt.scm\"";
				//		 cmd = "text2wave -o " + audio + " " + text + " -eval jdt.scm";					
					}
					else if(voice.equals("voice_akl_nz_cw_cg_cg")) {
						 voiceFile = "\"Voice" + File.separatorChar + "cw.scm\"";
				//		 cmd = "text2wave -o " + audio + " " + text + " -eval cw.scm";					
					}				
					else {
						 voiceFile = "\"Voice" + File.separatorChar + "kal.scm\"";
					//	cmd = "text2wave -o " + audio + " " + text + " -eval kal.scm";						
					}
					
					cmd = "text2wave -o " + audio + " " + text + " -eval "+voiceFile;					

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

		//String dir = "Audio" + File.separatorChar + term + "+([[:digit:]]).wav";
		String audio = "\"Audio" + File.separatorChar + term +".wav\"";



		ExecutorService createWorker = Executors.newSingleThreadExecutor(); 
		Task<File> task = new Task<File>() {
			@Override
			protected File call() throws Exception {
				try { 
					String cmd = "for f in Audio/*.wav; do echo \"file '$f'\" >> mylist.txt ; done ; ffmpeg -y -f concat -i mylist.txt -c copy " + audio + "; rm mylist.txt";
					//for f in Audio/apple+([[:digit:]]).wav;do echo "file '$f'" >> mylist.txt ; done ; ffmpeg -y -f concat -i mylist.txt -c copy "Audio/apple.wav"; rm mylist.txt


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


