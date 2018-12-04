/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc455_wpac;

import static csc455_wpac.CSC455_DatabaseProject.getResult;
import static csc455_wpac.CSC455_DatabaseProject.*;
import static csc455_wpac.CSC455_DatabaseProject.getSectionAvailability;
import static csc455_wpac.FXMLEventsController.eid;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author JordanKayleeVanVoorhis
 */
public class FXMLSeatingController implements Initializable {

    static String section;
    ObservableList<Integer> selectRowList = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10);
    ObservableList<Integer> selectSeatList = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10);
    static int row = 0;
    static int seat = 0;
    static String ename = null;
    static Date edate = null;

    @FXML
    private Button A;

    @FXML
    private Button B;

    @FXML
    private Button C;

    @FXML
    private Button D;

    @FXML
    private Button E;

    @FXML
    private Button F;

    @FXML
    private Button G;

    @FXML
    private AnchorPane sections;
    
    @FXML
    private AnchorPane standardSection;
        
    @FXML
    private AnchorPane sideSelection;
            
    @FXML
    private Text sectionLetter;
    
    @FXML
    private ChoiceBox selectRow;
    
    @FXML
    private ChoiceBox selectSeat;
    
    @FXML
    private Label confirmFirst;

    @FXML
    private void selectSection(ActionEvent e) throws SQLException, Exception{
        Button button = (Button) e.getSource();
        section = button.getId();
        System.out.println(section);
        ResultSet validSec = getSectionAvailability(eid, section);
        ResultSetMetaData md = validSec.getMetaData();
        int columns = md.getColumnCount();
        int count = 0;
        while (validSec.next()){
            for (int i = 1; i <= columns; i++){
                count = validSec.getInt(i);
            }
        }
        if (count == 0){
            System.out.println("No seats available in this section.");
        }
        else{
            sections.setVisible(false);
            standardSection.setVisible(true);
            sectionLetter.setText(section);
            sideSelection.setVisible(true);
            try {
                setAvailableRows();
            } catch (SQLException ex) {
                Logger.getLogger(FXMLSeatingController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void goBackToEvent(ActionEvent e) throws IOException{
        ((Node) e.getSource()).getScene().getWindow().hide();
        Parent event = FXMLLoader.load(getClass().getResource("FXMLEvents.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(event));
        stage.show();
    }
    
    @FXML
    private void goBackToSection(ActionEvent e){
        sections.setVisible(true);
        standardSection.setVisible(false);
        sideSelection.setVisible(false);
    }
    
    @FXML
    private void setAvailableRows() throws SQLException, Exception{
        String getRows = "distinct ROW_ID";
        String specifySection = " and SEC_ID = '" + section + "'";
        ResultSet availableRows = getRowAvailability(eid, getRows, specifySection);
        ResultSetMetaData md = availableRows.getMetaData();
        int columns = md.getColumnCount();
        selectRowList.clear();
        while (availableRows.next()){
            for (int i = 1; i <= columns; i++){
                selectRowList.add(availableRows.getInt(i));
            }
        }
        initializeRow();
    }
    
    @FXML
    private void initializeRow() throws SQLException, Exception{
        selectRow.setValue(selectRowList.get(0));
        selectRow.setItems(selectRowList);
        initializeSeat();
    }
    
    @FXML
    private void initializeSeat() throws SQLException, Exception{
        setAvailableSeats((int) selectRow.getValue());
        selectSeat.setValue(selectSeatList.get(0));
        selectSeat.setItems(selectSeatList);
    }
    
    @FXML
    private void setAvailableSeats(int currentRow) throws SQLException, Exception{
        String getSeats = "SEAT_NUMBER";
        String specifySection = " and SEC_ID = '" + section + "' and ROW_ID = " + currentRow + ";";
        ResultSet availableRows = getRowAvailability(eid, getSeats, specifySection);
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
    private void confirmRow(ActionEvent e) throws SQLException, Exception{
        confirmFirst.setVisible(false);
        row = (int) selectRow.getValue();
        //System.out.println(row);
        initializeSeat();
    }
    
    @FXML
    private void next(ActionEvent e) throws IOException{
        if (row != (int) selectRow.getValue()){
            confirmFirst.setVisible(true);
        }
        else{
            FXMLPurchaseTicketController.setRowNum(row);
            FXMLPurchaseTicketController.setSeatNum((int) selectSeat.getValue());
            FXMLPurchaseTicketController.setSec(section);
            FXMLPurchaseTicketController.setEname(ename);
            FXMLPurchaseTicketController.setEdate(edate);
            ((Node) e.getSource()).getScene().getWindow().hide();
            Parent ticket = FXMLLoader.load(getClass().getResource("FXMLPurchaseTicket.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(ticket));
            stage.show();
        }
    }
    
    @FXML
    private Label event;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        String query = "SELECT ENAME, EDATE from Event where EVENT_ID = " + Integer.toString(eid) + ";";
        ResultSet eventSet = null;
        try {
            eventSet = getResult(query);
            ResultSetMetaData md = eventSet.getMetaData();
            int columns = md.getColumnCount();
            while (eventSet.next()){
                for (int i = 1; i <= columns; i++){
                    if (i == 1){
                        ename = eventSet.getString(i);
                    }
                    if (i == 2){
                        edate = eventSet.getDate(i);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(FXMLSeatingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        event.setText(ename + "\n" + edate.toLocalDate().format(DateTimeFormatter.ofPattern("MMM d, uuuu")));
        sections.setVisible(true);
        standardSection.setVisible(false);
        sideSelection.setVisible(false);
        confirmFirst.setVisible(false);
    }
}
