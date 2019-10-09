package application;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 * View-Controller for the person table.
 * 
 */
public class MainEventHandlingController {
	
	@FXML
	private Button salesBtn;
	
	@FXML
	private Button customerBtn;
	
	@FXML
	private Button employeeBtn;
	
	@FXML
	private Button productBtn;
	
	private Pane rootLayout;
	
	@FXML
    private void initialize() {
    }
	

	@FXML
	private void handleSalesBtnAction(ActionEvent event) {
		try {
			// Load root layout from fxml file.
		   FXMLLoader loader = new FXMLLoader();
           loader.setLocation(Main.class.getResource("salesList.fxml"));
           rootLayout = loader.load();
           salesBtn.getScene().setRoot(rootLayout);
  
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@FXML
	private void handleCustomerBtnAction(ActionEvent event) {
		try {
			   FXMLLoader loader = new FXMLLoader();
	           loader.setLocation(Main.class.getResource("customerList.fxml"));
	           rootLayout = loader.load();
	           salesBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleEmployeeBtnAction(ActionEvent event) {
		try {
			   FXMLLoader loader = new FXMLLoader();
	           loader.setLocation(Main.class.getResource("employeeList.fxml"));
	           rootLayout = loader.load();
	           salesBtn.getScene().setRoot(rootLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleProductBtnAction(ActionEvent event) {
		try {
			   FXMLLoader loader = new FXMLLoader();
	           loader.setLocation(Main.class.getResource("productList.fxml"));
	           rootLayout = loader.load();
	           salesBtn.getScene().setRoot(rootLayout);
  
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}