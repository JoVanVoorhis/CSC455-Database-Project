/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc455_wpac;

import static csc455_wpac.CSC455_DatabaseProject.getResult;
import static csc455_wpac.CSC455_DatabaseProject.getRowAvailability;
import static csc455_wpac.CSC455_DatabaseProject.getSectionAvailability;
import static csc455_wpac.FXMLEventsController.eid;
import static csc455_wpac.FXMLPurchaseTicketController.sec;
import static csc455_wpac.FXMLSeatingController.row;
import static csc455_wpac.FXMLSeatingController.section;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author JordanKayleeVanVoorhis
 */
public class FXMLPurchaseTicketDirectlyController implements Initializable {
    String eventName;
    Date eventDate;
    int eid;
    String secID;
    int rowNum;
    int seatNum;
    
    ArrayList<Integer> ids = new ArrayList<>();
    ObservableList<String> eventsList = FXCollections.observableArrayList();
    ObservableList<String> selectSecList = FXCollections.observableArrayList("A","B","C","D","E","F","G");
    ObservableList<Integer> selectRowList = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10);
    ObservableList<Integer> selectSeatList = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10);
    
    @FXML
    private ChoiceBox event;
    
    @FXML
    private ChoiceBox section;
    
    @FXML
    private ChoiceBox row;
    
    @FXML
    private ChoiceBox seat;
    
    @FXML
    private Text confirmFirst;
    
    @FXML
    private void confirmEventAndDate(ActionEvent e) throws Exception{
        confirmFirst.setVisible(false);
        eid = ids.indexOf(eventsList.indexOf(event.getValue()));
        initializeSection();
    }
    
    @FXML
    private void confirmSec(ActionEvent e) throws Exception{
        if (eid != ids.indexOf(eventsList.indexOf(event.getValue()))){
            confirmFirst.setVisible(true);
        }
        else{
            confirmFirst.setVisible(false);
            secID = (String) section.getValue();
            initializeRow();
        }
    }
    
    @FXML
    private void confirmRow(ActionEvent e) throws SQLException, Exception{
        if (eid != ids.indexOf(eventsList.indexOf(event.getValue())) || secID != section.getValue()){
            confirmFirst.setVisible(true);
        }
        else{
            confirmFirst.setVisible(false);
            rowNum = (int) row.getValue();
            initializeSeat();
        }
    }
    
    @FXML
    private void confirmSeat(ActionEvent e){
        if (eid != ids.indexOf(eventsList.indexOf(event.getValue())) || secID != section.getValue() || rowNum != (int) row.getValue()){
            confirmFirst.setVisible(true);
        }
        else{
            confirmFirst.setVisible(false);
            seatNum = (int) seat.getValue();
        }
    }
    
    @FXML
    private void initializeSection() throws Exception{
        System.out.println(ids.get(eventsList.indexOf(event.getValue())));
        setAvailableSections(ids.get(eventsList.indexOf(event.getValue())));
        event.setValue(selectSecList.get(0));
        event.setItems(selectRowList);
    }
        
    @FXML
    private void initializeRow() throws SQLException, Exception{
        System.out.println((String) section.getValue());
        setAvailableRows((String) section.getValue());
        row.setValue(selectRowList.get(0));
        row.setItems(selectRowList);
    }
    
    @FXML
    private void initializeSeat() throws SQLException, Exception{
        System.out.println((int) row.getValue());
        setAvailableSeats((int) row.getValue());
        seat.setValue(selectSeatList.get(0));
        seat.setItems(selectSeatList);
    }
    
    @FXML
    private void next(ActionEvent e) throws IOException{
        if (seatNum != (int) seat.getValue()){
            confirmFirst.setVisible(true);
        }
        else{
            FXMLPurchaseTicketController.setRowNum(rowNum);
            FXMLPurchaseTicketController.setSeatNum(seatNum);
            FXMLPurchaseTicketController.setSec(secID);
            FXMLPurchaseTicketController.setEname(eventName);
            FXMLPurchaseTicketController.setEdate(eventDate);
            ((Node) e.getSource()).getScene().getWindow().hide();
            Parent ticket = FXMLLoader.load(getClass().getResource("FXMLPurchaseTicket.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(ticket));
            stage.show();
        }
    }
    
    @FXML
    private void setAvailableSections(int eventID){
        try {
            ResultSet validSec = getResult("SELECT SEC_ID FROM Ticket JOIN Seat ON TSEAT_ID = SEAT_ID Where TAVAILABILITY = 'Y' AND TEVENT_ID = " + eid + ";"); // This is where the error is.
            ResultSetMetaData md = validSec.getMetaData();
            int columns = md.getColumnCount();
            int count = 0;
            System.out.println("Getting section");
            while (validSec.next()){
                for (int i = 1; i <= columns; i++){
                    System.out.println(validSec.getString(i));
                    selectSecList.add(validSec.getString(i));
                }
            }
            initializeRow();
        } catch (Exception ex) {
            Logger.getLogger(FXMLSeatingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void setAvailableRows(String tempSec) throws SQLException, Exception{
        String getRows = "distinct ROW_ID";
        String specifySection = " and SEC_ID = '" + tempSec + "'";
        ResultSet availableRows = getRowAvailability(eid, getRows, specifySection);
        ResultSetMetaData md = availableRows.getMetaData();
        int columns = md.getColumnCount();
        selectRowList.clear();
        while (availableRows.next()){
            for (int i = 1; i <= columns; i++){
                selectRowList.add(availableRows.getInt(i));
            }
        }
        initializeSeat();
    }
    
    @FXML
    private void setAvailableSeats(int currentRow) throws SQLException, Exception{
        String getSeats = "SEAT_NUMBER";
        String specifySection = " and SEC_ID = '" + section + "' and ROW_ID = " + currentRow + ";";
        ResultSet availableRows = getRowAvailability(eid, getSeats, specifySection); // This needs to be changed to a stored event id value.
        ResultSetMetaData md = availableRows.getMetaData();
        int columns = md.getColumnCount();
        selectSeatList.clear();
        while (availableRows.next()){
            for (int i = 1; i <= columns; i++){
                selectSeatList.add(availableRows.getInt(i));
            }
        }
    }
    
    @FXML
    private void initializeList(){
        event.setItems(eventsList);
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        confirmFirst.setVisible(false);
        try {
            ResultSet allEvents = getResult("SELECT * from Event;"); // need to change this to upcoming events.
            ResultSetMetaData md = allEvents.getMetaData();
            int columns = md.getColumnCount();
            while (allEvents.next()){
                int id = 0;
                String name = null;
                Date date = null;
                //String image = null;
                for (int i = 1; i <= columns; i++){
                    if (i == 1){
                        ids.add(allEvents.getInt(i));
                    }
                    else if (i == 2){
                        name = allEvents.getString(i);
                    }
                    else if (i == 3){
                        date = allEvents.getDate(i);
                    }
                }
                eventsList.add(name + " - " + date.toLocalDate().format(DateTimeFormatter.ofPattern("MMM d, uuuu")));
            }
            initializeList();
        } catch (Exception ex) {
            Logger.getLogger(FXMLEventsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
