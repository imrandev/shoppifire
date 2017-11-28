package com.nerdgeeks.shop;

import com.nerdgeeks.shop.Util.FireDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;


public  class MainApp extends Application {

    public static Stage mainStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Initialize the firebase database with the default app
        FireDatabase fireDatabase = new FireDatabase();
        fireDatabase.ConnectFirebase();

        mainStage =primaryStage;
        URL myFxmlURL = ClassLoader.getSystemResource("Layout/login.fxml");
        Parent root = FXMLLoader.load(myFxmlURL);
        primaryStage.setTitle("Super Shop");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
