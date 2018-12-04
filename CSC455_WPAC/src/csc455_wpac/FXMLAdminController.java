/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc455_wpac;

import static csc455_wpac.CSC455_DatabaseProject.executeChangeEventDate;
import static csc455_wpac.CSC455_DatabaseProject.executeCreateEvent;
import static csc455_wpac.CSC455_DatabaseProject.executeQuery;
import static csc455_wpac.CSC455_DatabaseProject.getResult;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
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
    private Pane addEventPane;
    
    @FXML
    private TextField eventName;
    
    @FXML
    private Text didNotEnterName;
    
    @FXML
    private DatePicker eventDate;
    
    @FXML
    private Text didNotEnterDate;
    
    @FXML
    private Text eventDateTaken;
    
    @FXML
    private Pane eventAddedPane;
    
    @FXML
    private Text addedNewEventID;
    
    @FXML
    private Text newEventName;
    
    @FXML
    private Text newEventDate;
    
    @FXML
    private void addEvent(ActionEvent event){
        eventName.clear();
        didNotEnterName.setVisible(false);
        didNotEnterDate.setVisible(false);
        eventDateTaken.setVisible(false);
        eventAddedPane.setVisible(false);
        addEventPane.setVisible(true);
    }
    
    @FXML
    private void cancelAddEventButton(ActionEvent event){
        addEventPane.setVisible(false);
    }
    
    @FXML
    private void addEventButton(ActionEvent event) throws Exception{
        if ((eventName.getText() == null || eventName.getText().trim().isEmpty()) && eventDate.getValue() == null){
            didNotEnterDate.setVisible(true);
            didNotEnterName.setVisible(true);
        }
        else if ((eventName.getText() == null || eventName.getText().trim().isEmpty()) && eventDate.getValue() != null){
            didNotEnterName.setVisible(true);
            didNotEnterDate.setVisible(false);
        }
        else if (eventDate.getValue() == null && eventName.getText() != null){
            didNotEnterDate.setVisible(true);
            didNotEnterName.setVisible(false);
        }
        else if (eventName.getText() != null && eventDate.getValue() != null){
            int newEventID = 0;
            didNotEnterName.setVisible(false);
            didNotEnterDate.setVisible(false);
            executeCreateEvent(eventName.getText(), Date.valueOf(eventDate.getValue()));
            ResultSet result = getResult("SELECT EVENT_ID from Event WHERE ENAME = '" + eventName.getText() + "' AND EDATE = '" + Date.valueOf(eventDate.getValue()) + "';");
            ResultSetMetaData md = result.getMetaData();
            int columns = md.getColumnCount();
            while (result.next()){
                for (int i = 1; i <= columns; i++){
                    newEventID = result.getInt(i);
                }
            }
            if (newEventID == 0){
                eventDateTaken.setVisible(true);
            }
            else{
                eventDateTaken.setVisible(false);
                eventAddedPane.setVisible(true);
                addedNewEventID.setText(String.valueOf(newEventID));
                newEventName.setText(eventName.getText());
                newEventDate.setText(eventDate.getValue().format(DateTimeFormatter.ofPattern("MMM d, uuuu")));
                
            }
        }
    }
    
    @FXML
    private void okayButtonAction(ActionEvent event){
        eventAddedPane.setVisible(false);
        addEventPane.setVisible(false);
    }
        
    
    // Manage Events > Move Event > Move Event Pane
    
    @FXML
    private Pane moveEventPane;
    
    @FXML
    private Pane eventMovedPane;
    
    @FXML
    private TextField moveEventID;
    
    @FXML
    private Text mustEnterID;
    
    @FXML
    private DatePicker moveToDate;
    
    @FXML
    private Text mustEnterDate;
    
    @FXML
    private Text eventAlreadyScheduled;
    
    @FXML
    private Text movedEventID;
    
    @FXML
    private Text movedEventName;
    
    @FXML
    private Text movedEventDate;
    
    @FXML
    private Text enteredInvalidEventID;
    
    @FXML
    private void moveEvent(ActionEvent event){
        moveEventID.clear();
        eventMovedPane.setVisible(false);
        enteredInvalidEventID.setVisible(false);
        mustEnterID.setVisible(false);
        mustEnterDate.setVisible(false);
        eventAlreadyScheduled.setVisible(false);
        moveEventPane.setVisible(true);
    }
    
    @FXML
    private void cancelMoveEventButton(ActionEvent event){
        moveEventPane.setVisible(false);
    }
    
    @FXML
    private void moveEventButton(ActionEvent event) throws Exception{
        if ((moveEventID.getText() == null || moveEventID.getText().trim().isEmpty()) && moveToDate.getValue() == null){
            mustEnterID.setVisible(true);
            mustEnterDate.setVisible(true);
        }
        else if ((moveEventID.getText() == null || moveEventID.getText().trim().isEmpty()) && moveToDate.getValue() != null){
            mustEnterID.setVisible(true);
            mustEnterDate.setVisible(false);
        }
        else if (moveToDate.getValue() == null && moveEventID.getText() != null){
            mustEnterDate.setVisible(true);
            mustEnterID.setVisible(false);
        }
        else{
            boolean validID = false;
            ResultSet eids = getResult("select EVENT_ID from Event;");
            ResultSetMetaData md1 = eids.getMetaData();
            int columns1 = md1.getColumnCount();
            while (eids.next()){
                for (int i = 0; i <= columns1; i++){
                    if (i == 1 && Integer.valueOf(moveEventID.getText()) == eids.getInt(i)){
                        validID = true;
                        break;
                    }
                }
            }
            if (validID == true){
                String moveEventName = null;
                executeChangeEventDate(Date.valueOf(moveToDate.getValue()),Integer.valueOf(moveEventID.getText()));
                ResultSet result = getResult("SELECT ENAME from Event WHERE EVENT_ID = " + Integer.valueOf(moveEventID.getText()) + " AND EDATE = '" + Date.valueOf(moveToDate.getValue()) + "';");
                ResultSetMetaData md = result.getMetaData();
                int columns = md.getColumnCount();
                while (result.next()){
                    for (int i = 1; i <= columns; i++){
                        moveEventName = result.getString(i);
                        System.out.println(moveEventName);
                    }
                }
                if (moveEventName == null){
                    eventAlreadyScheduled.setVisible(true);
                }
                else{
                    eventMovedPane.setVisible(true);
                    movedEventID.setText(String.valueOf(moveEventID.getText()));
                    movedEventName.setText(moveEventName);
                    movedEventDate.setText(moveToDate.getValue().format(DateTimeFormatter.ofPattern("MMM d, uuuu")));
                    
                }
            }
            else{
                enteredInvalidEventID.setVisible(true);
            }
        }
    }
    
    @FXML
    private void okayedMoveButton(ActionEvent event){
        eventMovedPane.setVisible(false);
        moveEventPane.setVisible(false);
    }
    
    
    // Manage Events > Move Event > Update Ticket Prices Class
    
    @FXML
    private void updateTicketPrices(ActionEvent event){
        Stage stage = new Stage();
        stage.show();
    }
    
    
    // Manage Events > Delete Events > Delete Event Pane
    
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
        eventTitle.setVisible(false);
        areYouSure.setVisible(false);
        confirmFirst.setVisible(false);
        invalidEventID.setVisible(false);
        confirmDeleteEvent.setVisible(true);
    }
    
    @FXML
    private void cancelDeleteEventAction(ActionEvent event){
        confirmDeleteEvent.setVisible(false);
        
    }
    
    @FXML
    private void confirmButtonAction(ActionEvent event) throws Exception{
        if (eventIDField.getText() != null && !eventIDField.getText().trim().isEmpty()){
            String eventName = null;
            eventID = Integer.valueOf(eventIDField.getText());
            ResultSet result = getResult("SELECT EVENT_ID, ENAME from Event");
            ResultSetMetaData md = result.getMetaData();
            int columns = md.getColumnCount();
            while (result.next()){
                for (int i = 1; i <= columns; i++){
                    if (i == 1 && result.getInt(i) == eventID){
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
        CSC455_DatabaseProject.executeDeleteEvent(eventID);
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
        areYouSureDC.setVisible(false);
        confirmCustomerFirst.setVisible(false);
        invalidCustomerID.setVisible(false);
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
                    if (i == 1 && result.getInt(i) == customerID) {
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
        CSC455_DatabaseProject.executeDeleteCustomer(customerID);
        areYouSureDC.setVisible(false);
        customerName.setVisible(false);
        invalidCustomerID.setVisible(false);
        customerIDField.clear();
        confirmDeleteCustomer.setVisible(false);
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
        moveEventPane.setVisible(false);
        invalidEventID.setVisible(false);
        eventAlreadyScheduled.setVisible(false);
        mustEnterID.setVisible(false);
        mustEnterDate.setVisible(false);
        customerName.setVisible(false);
        confirmDeleteEvent.setVisible(false);
        confirmDeleteCustomer.setVisible(false);
        addEventPane.setVisible(false);
    }    
    
}
