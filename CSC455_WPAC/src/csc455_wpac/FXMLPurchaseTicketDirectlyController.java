/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc455_wpac;

import static csc455_wpac.CSC455_DatabaseProject.getRowAvailability;
import static csc455_wpac.FXMLEventsController.eid;
import static csc455_wpac.FXMLPurchaseTicketController.sec;
import static csc455_wpac.FXMLSeatingController.row;
import static csc455_wpac.FXMLSeatingController.section;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ResourceBundle;
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
    
    String secID;
    int rowNum;
    int seatNum;
    
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
    private Text invalidChoices;
    
    @FXML
    private Text noneAvailable;
    
    @FXML
    private Text confirmFirst;
    
    @FXML
    private void confirmEventAndDate(ActionEvent e){
        confirmFirst.setVisible(false);
        
    }
    
    @FXML
    private void confirmSec(ActionEvent e) throws Exception{
        confirmFirst.setVisible(false);
        secID = (String) section.getValue();
        initializeRow();
    }
    
    @FXML
    private void confirmRow(ActionEvent e) throws SQLException, Exception{
        confirmFirst.setVisible(false);
        rowNum = (int) row.getValue();
        initializeSeat();
    }
    
    @FXML
    private void confirmSeat(ActionEvent e){
        confirmFirst.setVisible(false);
        seatNum = (int) seat.getValue();
    }
    
        
    @FXML
    private void initializeRow() throws SQLException, Exception{
        setAvailableRows((String) section.getValue());
        row.setValue(selectRowList.get(0));
        row.setItems(selectRowList);
        initializeSeat();
    }
    
    @FXML
    private void initializeSeat() throws SQLException, Exception{
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
        if (selectRowList.isEmpty()){
            noneAvailable.setVisible(true);
        }
        else{
            initializeRow();
        }
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
        if (selectSeatList.isEmpty()){
            noneAvailable.setVisible(true);
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        invalidChoices.setVisible(false);
    }    
    
}
