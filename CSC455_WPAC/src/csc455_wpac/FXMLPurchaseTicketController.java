/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc455_wpac;

import static csc455_wpac.CSC455_DatabaseProject.getResult;
import static csc455_wpac.CSC455_DatabaseProject.getSpecificSeatAvailability;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author JordanKayleeVanVoorhis
 */
public class FXMLPurchaseTicketController implements Initializable {
    int tid = 0;
    int sid = 0;
    int eid = FXMLEventsController.eid;
    int rowNum = FXMLSeatingController.row;
    int seatNum = FXMLSeatingController.seat;
    String sec = FXMLSeatingController.section;
    String ename = FXMLSeatingController.ename;
    Date edate = FXMLSeatingController.edate;

    @FXML
    private Text price;
    
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
    private Text noLongerAvailable;
    
    @FXML
    private Button purchaseTicket;
    
    @FXML
    private Button returnToMain;
    
    @FXML
    private boolean checkAvailability() throws SQLException, Exception{
        return getSpecificSeatAvailability(sec, rowNum, seatNum, eid);
    }
    
    @FXML
    private void getSeatID() throws Exception{
        ResultSet result = getResult("SELECT SEAT_ID from Seat where SEC_ID = '" + sec + "' and ROW_ID = " + rowNum + " and SEAT_NUMBER = " + seatNum + ";");
        ResultSetMetaData md = result.getMetaData();
        int columns = md.getColumnCount();
        while (result.next()){
            for (int i = 1; i <= columns; i++){
                sid = result.getInt(i);
            }
        }
    }
    
    @FXML
    private void getTicketID() throws SQLException, Exception{
        ResultSet result = getResult("SELECT TICKET_ID from Ticket where TEVENT_ID = " + eid + " and TSEAT_ID = " + sid + ";");
        ResultSetMetaData md = result.getMetaData();
        int columns = md.getColumnCount();
        while (result.next()){
            for (int i = 1; i <= columns; i++){
                tid = result.getInt(i);
            }
        }
    }
    
    @FXML
    private void purchaseTicket(ActionEvent e) throws Exception{
        boolean goAhead = checkAvailability();
        if (goAhead){
            getSeatID();
            getTicketID();
            System.out.println(tid);
            CSC455_DatabaseProject.executeMakeTransaction(tid,FXMLDocumentController.customerID); //executePurchaseTicket(tid, sec, rowNum, seatNum, FXMLDocumentController.customerID); 
            ((Node) e.getSource()).getScene().getWindow().hide();
            URL url = getClass().getResource("FXMLTicket.fxml");
            if (url == null){
                System.out.println("Could not display ticket.");
            }
            Parent root = FXMLLoader.load(url);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        if (!goAhead){
            purchaseTicket.setVisible(false);
            noLongerAvailable.setVisible(true);
            returnToMain.setVisible(true);
        }
    }
    
    @FXML
    private void returnToMain(ActionEvent event) throws IOException{
        ((Node) event.getSource()).getScene().getWindow().hide();
        Parent customer = FXMLLoader.load(getClass().getResource("FXMLCustomerController.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(customer));
        stage.show();
    }
    
    @FXML
    private int getPrice() throws Exception{
        return CSC455_DatabaseProject.executeSecPrice(eid, sec);
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        noLongerAvailable.setVisible(false);
        returnToMain.setVisible(false);
        purchaseTicket.setVisible(true);
        try {
            price.setText("Price: $" + String.valueOf(getPrice()));
        } catch (Exception ex) {
            Logger.getLogger(FXMLPurchaseTicketController.class.getName()).log(Level.SEVERE, null, ex);
        }
        show.setText("Show: " + ename);
        date.setText("Date: " + edate.toLocalDate().format(DateTimeFormatter.ofPattern("MMM d, uuuu")));
        section.setText("Section: " + sec);
        row.setText("Row: " + String.valueOf(rowNum));
        seat.setText("Seat: " + String.valueOf(seatNum));
    }    
    
}
