package com.nerdgeeks.shop.Controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.nerdgeeks.shop.MainApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MainAppController implements Initializable{

    public JFXTextField usernameField;
    public JFXPasswordField passwordField;
    private String email,pwd;
    static String loggedInUserId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void loginAction(ActionEvent actionEvent) throws ExecutionException, InterruptedException {

        email = usernameField.getText();
        pwd = passwordField.getText();

        showAdminPanel(actionEvent);

//        if(!verification())
//            return;

//        UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmailAsync(email).get();
//        loggedInUserId = userRecord.getUid();
//        if(userRecord.getDisplayName().equals(pwd)){
//            if(userRecord.getUid().contains("emp")){
//                showEmpPanel();
//            } else {
//                showAdminPanel();
//            }
//        } else {
//            System.out.print("Login Failed");
//        }

    }

    @FXML
    public void cancelAction(ActionEvent event) {
        resetFields();
    }

    @FXML
    public void closeAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void minusAction(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void showEmpPanel(ActionEvent actionEvent) {

    }

    @FXML
    private void showAdminPanel(ActionEvent actionEvent){
        windows("layout/Admin.fxml","Admin Panel",actionEvent);
    }

    private void windows(String path, String title, ActionEvent event) {
        try {
            URL myFxmlURL = ClassLoader.getSystemResource(path);
            Parent root = FXMLLoader.load(myFxmlURL);
            Scene scene = new Scene(root);
            //Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.getIcons().add(new Image("/images/logo.png"));
            stage.show();
            MainApp.mainStage.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean verification(){
        if(email.isEmpty()) {
            usernameField.setPromptText("Email Required");
            return false;
        }else if (pwd.isEmpty()) {
            passwordField.setPromptText("Password Required");
            return false;
        }
        return true;
    }

    private void resetFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}
