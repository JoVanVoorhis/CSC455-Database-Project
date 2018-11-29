/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc455_wpac;

import static csc455_wpac.CSC455_DatabaseProject.getResult;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author JordanKayleeVanVoorhis
 */
public class FXMLEventsController implements Initializable {
    
    ObservableList<String> events = FXCollections.observableArrayList();
    ObservableList<String> ogEvents = FXCollections.observableArrayList();
    ObservableList<Integer> ids = FXCollections.observableArrayList();
    ObservableList<Integer> ogIds = FXCollections.observableArrayList();
    static int eid = 0;
    
    @FXML
    private ListView list;
    
    @FXML
    private Button expand;
    
    @FXML
    private AnchorPane selectedEvent;
    
    @FXML
    private TextField searched;
    
    @FXML
    private DatePicker searchedDate;
        
    @FXML
    private AnchorPane searchBar;
    
    @FXML
    private ScrollPane eventsScroller;
    
    @FXML
    private void expandEvent(ActionEvent e){
        if (!list.getSelectionModel().isEmpty()){
            int event = list.getSelectionModel().getSelectedIndex();
            System.out.println(event);
            eid = ids.get(event);
            System.out.println(eid);
            eventsScroller.setVisible(false);
            expand.setVisible(false);
            selectedEvent.setVisible(true);
            // initialize image here!
        }
    }

    @FXML
    private void eventsHome(ActionEvent e){
        searched.clear();
        events.clear();
        ids.clear();
        for (Object event : ogEvents){
            events.add((String) event);
        }
        for (Object id : ogIds){
            ids.add((int) id);
        }
        initializeEvents();
    }
    
    @FXML
    private void goToSections(ActionEvent e) throws IOException{
        ((Node) e.getSource()).getScene().getWindow().hide();
        Parent sections = FXMLLoader.load(getClass().getResource("FXMLSeating.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(sections));
        stage.show();
    }
    
    @FXML
    private void initializeList(){
        list.setItems(events);
    }
    
    @FXML
    private void initializeEvents(){
        selectedEvent.setVisible(false);
        searchBar.setVisible(true);
        eventsScroller.setVisible(true);
        expand.setVisible(true);
    }
    
    @FXML
    private void searchEvents() throws SQLException, Exception{
        events.clear();
        ids.clear();
        ObservableList outcomes = FXCollections.observableArrayList();
        ResultSet allEvents = getResult("SELECT * from Event;");
        ResultSetMetaData md = allEvents.getMetaData();
        int columns = md.getColumnCount();
        while (allEvents.next()){
            for (int i = 1; i <= columns; i++){
                if (!searched.getText().isEmpty() && i == 2 && allEvents.getString(i).toLowerCase().contains(searched.getText().toLowerCase()) && !outcomes.contains(allEvents.getInt(1))){
                     outcomes.add(allEvents.getInt(1));
                }
                if (searchedDate.getValue() != null && i == 3 && allEvents.getDate(i).toLocalDate().equals(searchedDate.getValue()) && !outcomes.contains(allEvents.getInt(1))){
                    outcomes.add(allEvents.getInt(1));
                }
            }
        }
        if (outcomes.isEmpty()){
            events.add("Your search did not match any events.");
        }
        else {
            for (Object i : outcomes){
                ids.add((int)i);
                events.add(ogEvents.get(ogIds.indexOf((int) i)));
            }
        } 
        initializeList();
    }    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @SuppressWarnings("null")
    public void initialize(URL url, ResourceBundle rb) {
        initializeEvents();
        try {
            ResultSet allEvents = getResult("SELECT * from Event;");
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
                        ogIds.add(allEvents.getInt(i));
                    }
                    else if (i == 2){
                        name = allEvents.getString(i);
                        ogEvents.add(name);
                    }
                    else if (i == 3){
                        date = allEvents.getDate(i);
                    }
//                    else if (i == 4){
//                        image = allEvents.getString(i);
//                    }
                }
                events.add(name + " - " + date.toLocalDate().format(DateTimeFormatter.ofPattern("MMM d, uuuu")));
            }
            System.out.println("ogIds " + ogIds + "\nogEvents " + ogEvents);
            initializeList();
        } catch (Exception ex) {
            Logger.getLogger(FXMLEventsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
}
