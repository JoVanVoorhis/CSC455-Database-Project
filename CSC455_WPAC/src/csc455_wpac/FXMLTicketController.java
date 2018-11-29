/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc455_wpac;

import java.net.URL;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.text.*;

/**
 * FXML Controller class
 *
 * @author JordanKayleeVanVoorhis
 */
public class FXMLTicketController implements Initializable {
    int tid = 0;
    int eid = FXMLEventsController.eid;
    int rowNum = FXMLSeatingController.row;
    int seatNum = FXMLSeatingController.seat;
    String sec = FXMLSeatingController.section;
    String ename = FXMLSeatingController.ename;
    Date edate = FXMLSeatingController.edate;

    @FXML
    private Text barCode;
    
    @FXML
    private Text show;
    
    @FXML
    private Text date;
    
    @FXML
    private Text section;
    
    @FXML
    private Text row;
    
    @FXML
    private Text seat;
    
    @FXML
    private void printTicket(){
        barCode.setText(String.valueOf(eid) + String.valueOf(tid));
        show.setText(ename);
        date.setText(edate.toLocalDate().format(DateTimeFormatter.ofPattern("MMM d, uuuu")));
        section.setText(sec);
        row.setText(String.valueOf(rowNum));
        seat.setText(String.valueOf(seatNum));
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        printTicket();
    }    
    
}
