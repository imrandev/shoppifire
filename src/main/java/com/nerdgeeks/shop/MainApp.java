package com.nerdgeeks.shop;

import com.nerdgeeks.shop.Util.DatabaseUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;

import java.net.URL;


public  class MainApp extends Application {

    public static Stage mainStage;
    private double xOffset = 0;
    private double yOffset = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{

//        //Initialize the firebase database with the default app
        DatabaseUtil fireDatabase = new DatabaseUtil();
        fireDatabase.ConnectFirebase();

        mainStage =stage;
        URL myFxmlURL = ClassLoader.getSystemResource("layout/MainApp.fxml");
        Parent root = FXMLLoader.load(myFxmlURL);

        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        Scene scene = new Scene(root);
        stage.setTitle("Super Shop:: Version 1.0");
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
}
