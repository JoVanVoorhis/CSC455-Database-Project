/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc455_wpac;

import static csc455_wpac.CSC455_DatabaseProject.executeNewCustomer;
import static csc455_wpac.CSC455_DatabaseProject.getResult;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author JordanKayleeVanVoorhis
 */
public class FXMLDocumentController implements Initializable {
    
    static int customerID = 0;
    //@FXML
    //private Label label;
    
    ObservableList<String> accountTypeList = FXCollections.observableArrayList("Customer", "Administrator");
    
    @FXML
    private ChoiceBox<String> accountTypeBox;

    @FXML
     TextField identification;
    
    @FXML
    private PasswordField password;
    
    @FXML
    private Text incorrectLogin;
    
    @FXML
    private AnchorPane register;
    
    @FXML
    private Text invalidPassword;
    
    @FXML
    private TextField firstName;
    
    @FXML
    private TextField lastName;
    
    @FXML
    private PasswordField enterPassword;
    
    @FXML
    private PasswordField verifyPassword;
    
    @FXML
    private AnchorPane popUp;
    
    @FXML
    private Text id;
    
    // Question: What happens when someone enters a name and password that already exists?
    @FXML
    private void createAccount(ActionEvent event) throws ClassNotFoundException, Exception {
        if (!enterPassword.getText().equals(verifyPassword.getText())){
            System.out.println(enterPassword.getText() + ' ' + verifyPassword.getText());
            enterPassword.clear();
            verifyPassword.clear();
            invalidPassword.setVisible(true);
        }
        else{
            String cid = null;
//            ResultSet r1 = getResult("SELECT * from Customer;");
//            ResultSetMetaData md = r1.getMetaData();
//            int columns = md.getColumnCount();
//            while(r1.next()){
//                for (int i = 1; i <= columns; i++){
//                    System.out.print(r1.getString(i) + " ");
//                }
//                System.out.println("");
//            }
            executeNewCustomer(firstName.getText(), lastName.getText(), enterPassword.getText());
//            ResultSet r2 = getResult("SELECT * from Customer;");
//            ResultSetMetaData md1 = r2.getMetaData();
//            int columns1 = md1.getColumnCount();
//            while(r2.next()){
//                for (int i = 1; i <= columns1; i++){
//                    System.out.print(r2.getString(i) + " ");
//                }
//                System.out.println("");
//            }
            ResultSet result = getResult("SELECT CUSTOMER_ID from Customer where CFIRST_NAME = '" + firstName.getText() + "' and CLAST_NAME = '" + lastName.getText() + "' and CPASSWORD = '" + enterPassword.getText() + "';");
            ResultSetMetaData md2 = result.getMetaData();
            int columns2 = md2.getColumnCount();
            while(result.next()){
                for (int i = 1; i <= columns2; i++){
                    //System.out.print(result.getString(i) + " ");
                    cid = result.getString(i);
                }
                //System.out.println("");
            }
            id.setText(cid);
            popUp.setVisible(true);
        }
    }
    
    @FXML
    private void returnToLogin(ActionEvent event) throws IOException{
        popUp.setVisible(false);
        register.setVisible(false);
    }
    
    @FXML
    private void displayIncorrectLogin(){
        incorrectLogin.setVisible(true);
    }
    
    @FXML
    private void handleNewCustomer(ActionEvent event) throws IOException{
        register.setVisible(true);
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException{
        String name = null;
        String[] user;
        if ("Customer".equals(accountTypeBox.getValue())){
            try {
                ResultSet result = getResult("SELECT * from Customer");
                ResultSetMetaData md = result.getMetaData();
                int columns = md.getColumnCount();
                boolean found = false;
                while (result.next() && !found){
                    for (int i = 1; i <= columns; i++){
                        if (i == 1 && result.getString(i).equals(identification.getText())){
                            found = true;
                        }
                        else if (i == 4 && result.getString(i).equals(password.getText())){
                            found = true;
                            name = result.getString(2) + " " + result.getString(3);
                        }
                    }
                }
                if (name == null){
                    identification.clear();
                    password.clear();
                    displayIncorrectLogin();
                }
                else {
                    customerID = Integer.valueOf(identification.getText());
                    System.out.println("Welcome: " + name);
                    try{
                        ((Node) event.getSource()).getScene().getWindow().hide();
                        Parent customer = FXMLLoader.load(getClass().getResource("FXMLEvents.fxml"));//FXMLCustomer.fxml"));
                        Stage stage = new Stage();
                        stage.setScene(new Scene(customer));
                        stage.show();
                    }catch (Exception e){
                        System.out.println("Cannot load the customer window.");
                        Logger.getLogger(CSC455_WPAC.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(CSC455_WPAC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            try {
                ResultSet result = getResult("SELECT * from Admin");
                ResultSetMetaData md = result.getMetaData();
                int columns = md.getColumnCount();
                boolean found = false;
                while (result.next() && !found){
                    for (int i = 1; i <= columns; i++){
                        if (i == 1 && result.getString(i).equals(identification.getText())){
                            found = true;
                        }
                        else if (i == 3 && result.getString(i).equals(password.getText())){
                            found = true;
                            name = result.getString(2);
                        }
                    }
                }
                if (name == null){
                    identification.clear();
                    password.clear();
                    displayIncorrectLogin();
                }
                else {
                    System.out.println("Welcome: " + name);
                    try{
                        ((Node) event.getSource()).getScene().getWindow().hide();
                        Parent admin = FXMLLoader.load(getClass().getResource("FXMLAdmin.fxml"));
                        Stage stage = new Stage();
                        stage.setScene(new Scene(admin));
                        stage.show();
                    }catch (Exception e){
                        System.out.println("Cannot load the admin window.");
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(CSC455_WPAC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @FXML
    private void initialize(){
        register.setVisible(false);
        popUp.setVisible(false);
        invalidPassword.setVisible(false);
        incorrectLogin.setVisible(false);
        accountTypeBox.setValue("Customer");
        accountTypeBox.setItems(accountTypeList);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initialize();
    }    
    
}
