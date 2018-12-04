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
    static int eid;
    static int tid;
    static int rowNum;
    static int seatNum;
    static String sec;
    static String ename;
    static Date edate;
    
    public static void setEID(int id){
        eid = id;
    }
    
    public static void setTID(int id){
        tid = id;
    }
    
    public static void setRowNum(int num){
        rowNum = num;
    }
    
    public static void setSeatNum(int num){
        seatNum = num;
    }
    
    public static void setSec(String id){
        sec = id;
    }
    
    public static void setEname(String name){
        ename = name;
    }
    
    public static void setEdate(Date date){
        edate = date;
    }
    
    public static int getEID(){
        return eid;
    }
    
    public static int getTID(){
        return tid;
    }
    
    public static int getRowNum(){
        return rowNum;
    }
    
    public static int getSeatNum(){
        return seatNum;
    }
    
    public static String getSec(){
        return sec;
    }
    
    public static String getEname(){
        return ename;
    }
    
    public static Date getEdate(){
        return edate;
    }

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
        barCode.setText(String.valueOf(eid) + "" + String.valueOf(tid));
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
