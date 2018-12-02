/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc455_wpac;

import java.net.URL;
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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author JordanKayleeVanVoorhis
 */
public class FXMLCustomerController implements Initializable {
    static int cid = FXMLDocumentController.customerID;
    
    @FXML
    private Text name;
    
    @FXML
    private Pane confirmDelete;
    
    @FXML
    private void deleteAccountAction(ActionEvent event){
        confirmDelete.setVisible(true);
    }
    
    @FXML
    private void cancelDeleteAction(ActionEvent event){
        confirmDelete.setVisible(false);
    }
    
    @FXML
    private void confirmDeleteAction(ActionEvent event) throws Exception{
        CSC455_DatabaseProject.executeDeleteCustomer(cid);
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
    
    @FXML
    private void manageProfileAction(ActionEvent event){
        try{
            ((Node) event.getSource()).getScene().getWindow().hide();
            //Parent manage = FXMLLoader.load(getClass().getResource("FXMLManageProfile.fxml"));
            Stage stage = new Stage();
            //stage.setScene(new Scene(manage));
            stage.show();
        }catch (Exception e){
            System.out.println("Cannot load the manage profile window.");
            Logger.getLogger(CSC455_WPAC.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    @FXML
    private void customerEventsAction(ActionEvent event){
        try{
            ((Node) event.getSource()).getScene().getWindow().hide();
            //Parent cEvents = FXMLLoader.load(getClass().getResource("FXMLCustomerEvents.fxml"));
            Stage stage = new Stage();
            //stage.setScene(new Scene(cEvents));
            stage.show();
        }catch (Exception e){
            System.out.println("Cannot load the customer events window.");
            Logger.getLogger(CSC455_WPAC.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    @FXML
    private void returnTicketAction(ActionEvent event){
        try{
            ((Node) event.getSource()).getScene().getWindow().hide();
            //Parent return = FXMLLoader.load(getClass().getResource("FXMLTicketReturn.fxml"));
            Stage stage = new Stage();
            //stage.setScene(new Scene(return));
            stage.show();
        }catch (Exception e){
            System.out.println("Cannot load the return ticket window.");
            Logger.getLogger(CSC455_WPAC.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    @FXML
    private void ticketPurchaseAction(ActionEvent event){
        try{
            ((Node) event.getSource()).getScene().getWindow().hide();
            //Parent ticket = FXMLLoader.load(getClass().getResource("FXMLDirectTicket.fxml"));
            Stage stage = new Stage();
            //stage.setScene(new Scene(ticket));
            stage.show();
        }catch (Exception e){
            System.out.println("Cannot load the direct ticket window.");
            Logger.getLogger(CSC455_WPAC.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    @FXML
    private void eventsFindFriendsAction(ActionEvent event){
        try{
            ((Node) event.getSource()).getScene().getWindow().hide();
            //Parent friends = FXMLLoader.load(getClass().getResource("FXMLFindFriends.fxml"));
            Stage stage = new Stage();
            //stage.setScene(new Scene(friends));
            stage.show();
        }catch (Exception e){
            System.out.println("Cannot load the find friends window.");
            Logger.getLogger(CSC455_WPAC.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    @FXML
    private void eventsCalendarAction(ActionEvent event){
        try{
            ((Node) event.getSource()).getScene().getWindow().hide();
            Parent events = FXMLLoader.load(getClass().getResource("FXMLEvents.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(events));
            stage.show();
        }catch (Exception e){
            System.out.println("Cannot load the events window.");
            Logger.getLogger(CSC455_WPAC.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        name.setText(FXMLDocumentController.name + ",");
        confirmDelete.setVisible(false);
    }    
    
}
