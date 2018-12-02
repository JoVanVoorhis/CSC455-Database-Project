/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc455_wpac;

import static csc455_wpac.CSC455_DatabaseProject.getResult;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author JordanKayleeVanVoorhis
 */
public class FXMLAdminController implements Initializable {
    int eventID = 0;
    int customerID = 0;

    @FXML
    private Text name;
    
    
    // Manage Events > Delete Events > Delete Customer Pane
    
    @FXML
    private Pane confirmDeleteEvent;
    
    @FXML
    private Text confirmFirst;
    
    @FXML
    private Text areYouSure;
    
    @FXML
    private Text eventTitle;
    
    @FXML
    private Text invalidEventID;
    
    @FXML
    private TextField eventIDField;
    
    @FXML
    private void deleteEvent(ActionEvent event){
        confirmDeleteEvent.setVisible(true);
    }
    
    @FXML
    private void cancelDeleteEventAction(ActionEvent event){
        confirmDeleteEvent.setVisible(false);
    }
    
    @FXML
    private void confirmButtonAction(ActionEvent event) throws Exception{
        if (eventIDField != null){
            String eventName = null;
            eventID = Integer.valueOf(eventIDField.getText());
            ResultSet result = getResult("SELECT EVENT_ID, ENAME from Event");
            ResultSetMetaData md = result.getMetaData();
            int columns = md.getColumnCount();
            while (result.next()){
                for (int i = 1; i <= columns; i++){
                    if (result.getInt(i) == eventID){
                        invalidEventID.setVisible(false);
                        eventName = result.getString(2);
                        break;
                    }
                }
            }
            if (eventName != null){
                areYouSure.setVisible(true);
                eventTitle.setText(eventName + "?");
                eventTitle.setVisible(true);
            }
            else{
                eventIDField.clear();
                invalidEventID.setVisible(true);
            }
        }
    }
    
    @FXML
    private void confirmDeleteEventAction(ActionEvent event) throws Exception{
        CSC455_DatabaseProject.executeQuery("DELETE FROM Event WHERE EVENT_ID = " + eventID + ";");
        areYouSure.setVisible(false);
        eventTitle.setVisible(false);
        invalidEventID.setVisible(false);
        eventIDField.clear();
        confirmDeleteEvent.setVisible(false);
    }
    
    
    // Manage Customers > Delete Customer > Delete Customer Pane
    
    @FXML
    private Pane confirmDeleteCustomer;
    
    @FXML
    private Text confirmCustomerFirst;
    
    @FXML
    private Text areYouSureDC;
    
    @FXML
    private Text customerName;
    
    @FXML
    private Text invalidCustomerID;
    
    @FXML
    private TextField customerIDField;
    
    @FXML
    private void deleteCustomer(ActionEvent event){
        confirmDeleteCustomer.setVisible(true);
    }
    
    @FXML
    private void cancelDeleteCustomerAction(ActionEvent event){
        confirmDeleteCustomer.setVisible(false);
    }
    
    @FXML
    private void confirmDCButtonAction(ActionEvent event) throws Exception{
        if (customerIDField != null){
            String customerFullName = null;
            customerID = Integer.valueOf(customerIDField.getText());
            ResultSet result = getResult("SELECT CUSTOMER_ID, CFIRST_NAME, CLAST_NAME from Customer");
            ResultSetMetaData md = result.getMetaData();
            int columns = md.getColumnCount();
            while (result.next()){
                for (int i = 1; i <= columns; i++){
                    if (result.getInt(i) == customerID){
                        invalidCustomerID.setVisible(false);
                        customerFullName = result.getString(2) + " " + result.getString(3);
                        break;
                    }
                }
            }
            if (customerFullName != null){
                areYouSureDC.setVisible(true);
                customerName.setText(customerFullName + "?");
                customerName.setVisible(true);
            }
            else{
                customerIDField.clear();
                invalidCustomerID.setVisible(true);
            }
        }
    }
    
    @FXML
    private void confirmDeleteCustomerAction(ActionEvent event) throws Exception{
        CSC455_DatabaseProject.executeQuery("DELETE FROM Customer WHERE CUSTOMER_ID = " + customerID + ";");
        areYouSureDC.setVisible(false);
        customerName.setVisible(false);
        invalidCustomerID.setVisible(false);
        customerIDField.clear();
        confirmDeleteCustomer.setVisible(false);
    }
    
    // Log Out Button on bottom left side.
    
    @FXML
    private void logOutAction(ActionEvent event) throws Exception{
        ((Node) event.getSource()).getScene().getWindow().hide();
        URL url = getClass().getResource("FXMLDocument.fxml");
        if (url == null){
            System.out.println("Could not return to login.");
        }
        Parent root = FXMLLoader.load(url);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    // Manage Events > Add Event > Add Event Pane
    
    @FXML
    private void addEvent(ActionEvent event){
        Stage stage = new Stage();
        stage.show();
    }
    
    
    // Manage Events > Move Event > Move Event Pane
    
    @FXML
    private void moveEvent(ActionEvent event){
        Stage stage = new Stage();
        stage.show();
    }
    
    
    // Manage Events > Move Event > Move Event Pane
    
    @FXML
    private void updateTicketPrices(ActionEvent event){
        Stage stage = new Stage();
        stage.show();
    }
    
    
    // Search for Events > Search Event Information > Event Information Class
    
    @FXML
    private void searchEventInformation(ActionEvent event){
        Stage stage = new Stage();
        stage.show();
    }
    
    
    // Search for Events > Find Customers Attending an Event > Find Customers Class
    
    @FXML
    private void searchCustomersAttendingEvent(ActionEvent event){
        Stage stage = new Stage();
        stage.show();
    }
    
    
    // Manage Customers > Revoke Customer Ticket > Revoke Ticket Pane
    
    @FXML
    private void revokeCustomerTicket(ActionEvent event){
        Stage stage = new Stage();
        stage.show();
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        name.setText(FXMLDocumentController.name);
        areYouSure.setVisible(false);
        confirmFirst.setVisible(false);
        invalidEventID.setVisible(false);
        confirmDeleteEvent.setVisible(false);
        areYouSureDC.setVisible(false);
        confirmCustomerFirst.setVisible(false);
        invalidCustomerID.setVisible(false);
        confirmDeleteCustomer.setVisible(false);
    }    
    
}
