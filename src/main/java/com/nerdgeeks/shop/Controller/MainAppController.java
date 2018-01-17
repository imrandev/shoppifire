package com.nerdgeeks.shop.Controller;

import com.google.firebase.database.FirebaseDatabase;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.nerdgeeks.shop.MainApp;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MainAppController implements Initializable{

    public JFXTextField usernameField;
    public JFXPasswordField passwordField;
    public JFXButton loginButton;
    public HBox rootBox;
    private String email,pwd;
    static String loggedInUserId;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void loginAction(ActionEvent actionEvent) throws ExecutionException, InterruptedException {

        email = usernameField.getText();
        pwd = passwordField.getText();

        showEmpPanel();
        //showAdminPanel();

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
    private void showEmpPanel() {
        onFadeTransition("layout/sell/Sell.fxml");
    }

    private void onFadeTransition(String path) {
        FadeTransition ft = new FadeTransition();
        ft.setDuration(Duration.millis(500));
        ft.setNode(rootBox);
        ft.setFromValue(1);
        ft.setToValue(0.7);
        ft.setInterpolator(new Interpolator() {
            @Override
            protected double curve(double t) {
                return 0.5;
            }
        });
        ft.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //windows(path, "EMPLOYEE");
                loadNextScene(path);
            }
        });
        ft.play();
    }

    private void loadNextScene(String path) {
        try {
            Parent secondView;
            URL myFxmlURL = ClassLoader.getSystemResource(path);

            secondView = (VBox) FXMLLoader.load(myFxmlURL);

            Scene newScene = new Scene(secondView);

            Stage currentStage = (Stage) rootBox.getScene().getWindow();
            currentStage.setScene(newScene);
            //currentStage.setFullScreen(true);

            secondView.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            secondView.setOnMouseDragged((MouseEvent event) -> {
                currentStage.setX(event.getScreenX() - xOffset);
                currentStage.setY(event.getScreenY() - yOffset);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void windows(String path, String title) {
        try {
            URL myFxmlURL = ClassLoader.getSystemResource(path);
            Parent root = FXMLLoader.load(myFxmlURL);
            Scene scene = new Scene(root);
            //Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.getIcons().add(new Image("/images/logo.png"));
            stage.show();
            MainApp.mainStage.hide();

            root.setOnMousePressed((MouseEvent ev) -> {
                xOffset = ev.getSceneX();
                yOffset = ev.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent ev) -> {
                stage.setX(ev.getScreenX() - xOffset);
                stage.setY(ev.getScreenY() - yOffset);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showAdminPanel(){
        onFadeTransition("layout/Admin.fxml");
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
