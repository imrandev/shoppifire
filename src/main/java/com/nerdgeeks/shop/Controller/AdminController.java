package com.nerdgeeks.shop.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.nerdgeeks.shop.MainApp;
import com.nerdgeeks.shop.Util.JFXUtil;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class AdminController implements Initializable {

    public VBox mainPane;

    public Button dashboardButton, supplierButton, stockButton,
            productButton, purchaseButton, salesButton, staffButton, reportButton;
    public Label dateLabel;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button menu;
    @FXML
    private VBox drawer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dateLabel.setText(JFXUtil.getCurrentMonth());

        setDataPane(fadeAnimate("/layout/Dashboard/Dashboard.fxml"));

        menu.setOnAction((ActionEvent evt) -> {
            if (drawer.getTranslateX() != 0) {
                openNav();
            } else {
                closeNav();
            }
        });
    }

    private void openNav(){
        TranslateTransition openNav = new TranslateTransition(new Duration(350), drawer);
        openNav.setToX(0);
        openNav.play();
        menu.getStyleClass().remove("hamburger-button");
        menu.getStyleClass().add("open-menu");
    }

    private void closeNav(){
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), drawer);
        closeNav.setToX(-(drawer.getWidth()));
        closeNav.play();
        menu.getStyleClass().remove("open-menu");
        menu.getStyleClass().add("hamburger-button");
    }

    @FXML
    public void dashboardAction(ActionEvent actionEvent) {
        closeNav();
        setDataPane(fadeAnimate("/layout/Dashboard/Dashboard.fxml"));
        setNavigationButtonCurrentStateStyle(dashboardButton);
    }

    @FXML
    public void supplierAction(ActionEvent event) {
        closeNav();
        setDataPane(fadeAnimate("/layout/Supplier/Supplier.fxml"));
        setNavigationButtonCurrentStateStyle(supplierButton);
    }

    @FXML
    public void productAction(ActionEvent event) {
        closeNav();
        setDataPane(fadeAnimate("/layout/Product/Product.fxml"));
        setNavigationButtonCurrentStateStyle(productButton);
    }

    @FXML
    public void purchaseAction(ActionEvent event) {
        closeNav();
        setDataPane(fadeAnimate("/layout/Purchase/Purchase.fxml"));
        setNavigationButtonCurrentStateStyle(purchaseButton);
    }

    @FXML
    public void stockAction(ActionEvent event) {
        closeNav();
        setNavigationButtonCurrentStateStyle(stockButton);
    }

    @FXML
    public void salesAction(ActionEvent event) {
        closeNav();
        setNavigationButtonCurrentStateStyle(salesButton);
    }

    @FXML
    public void reportAction(ActionEvent event) {
        closeNav();
        setNavigationButtonCurrentStateStyle(reportButton);
    }

    @FXML
    public void staffAction(ActionEvent event) {
        closeNav();
        setNavigationButtonCurrentStateStyle(staffButton);
    }

    @FXML
    public void logoutAction(ActionEvent event) throws Exception {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("/layout/MainApp.fxml"));
        Stage stage = new Stage();
        MainApp.mainStage = stage;
        root.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
        Scene scene = new Scene(root);
        stage.setTitle("Inventory:: Version 1.0");
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    private void windows(String path, String title, ActionEvent event) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.setScene(scene);
        stage.show();
    }

    private void setNavigationButtonCurrentStateStyle(Node node){

        // Remove All button current state
        dashboardButton.getStyleClass().remove("menu-buttons-selected");
        supplierButton.getStyleClass().remove("menu-buttons-selected");
        stockButton.getStyleClass().remove("menu-buttons-selected");
        productButton.getStyleClass().remove("menu-buttons-selected");
        purchaseButton.getStyleClass().remove("menu-buttons-selected");
        salesButton.getStyleClass().remove("menu-buttons-selected");
        staffButton.getStyleClass().remove("menu-buttons-selected");
        reportButton.getStyleClass().remove("menu-buttons-selected");

        //set current button state
        node.getStyleClass().add("menu-buttons-selected");

    }

    private VBox fadeAnimate(String url) {
        VBox v = null;
        try {
            v = (VBox) FXMLLoader.load(getClass().getResource(url));
            VBox.setVgrow(v, Priority.ALWAYS);
            FadeTransition ft = new FadeTransition(Duration.millis(1500));
            ft.setNode(v);
            ft.setFromValue(0.4);
            ft.setToValue(1);
            ft.setCycleCount(1);
            ft.setAutoReverse(true);
            ft.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return v;
    }

    private void setDataPane(Node node) {
        // update VBox with new form(FXML) depends on which button is clicked
        mainPane.getChildren().setAll(node);
    }
}
