package com.nerdgeeks.shop.Controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.nerdgeeks.shop.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;


public class controllerLogin implements Initializable{

    public JFXTextField getEmail;
    public JFXPasswordField getPass;
    private String email,pwd;
    static String loggedInUserId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void login() throws ExecutionException, InterruptedException {
        email = getEmail.getText();
        pwd = getPass.getText();

        showAdminPanel();

//        if(!verification())
//            return;
//
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

    private void showEmpPanel() {
        try {
            URL myFxmlURL = ClassLoader.getSystemResource("Layout/EmployeeHome.fxml");
            Parent empView = FXMLLoader.load(myFxmlURL);
            Scene scene = new Scene(empView);
            Stage stage= new Stage();
            stage.setTitle("Employee Panel");
            stage.setScene(scene);
            stage.show();
            MainApp.mainStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAdminPanel(){
        try {
            String path = "Layout/admin.fxml";
            URL myFxmlURL = ClassLoader.getSystemResource(path);
            Parent adminView = FXMLLoader.load(myFxmlURL);
            Scene scene = new Scene(adminView);
            Stage stage= new Stage();
            stage.setTitle("Admin Panel");
            stage.setScene(scene);
            stage.show();
            MainApp.mainStage.close();
        } catch (IOException e) {
             e.printStackTrace();
        }
    }

    private boolean verification(){
        if(email.isEmpty()) {
            getEmail.setPromptText("Email Required");
            return false;
        }else if (pwd.isEmpty()) {
            getPass.setPromptText("Password Required");
            return false;
        }
        return true;
    }

}
