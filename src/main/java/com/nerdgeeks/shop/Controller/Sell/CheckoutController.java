package com.nerdgeeks.shop.Controller.Sell;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.nerdgeeks.shop.Model.CheckoutModel;
import com.nerdgeeks.shop.Model.SellProductModel;
import com.nerdgeeks.shop.Model.Stock;
import com.nerdgeeks.shop.Util.AppConstant;
import com.nerdgeeks.shop.Util.DatabaseUtil;
import com.nerdgeeks.shop.Util.JFXUtil;
import com.nerdgeeks.shop.Util.OnGetDataListener;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CheckoutController implements Initializable {

    public TextField totalAmountField;
    public TextField paidAmountField;
    public static double totalAmount;
    public JFXButton printButton;
    public static ObservableList<SellProductModel> Products = FXCollections.observableArrayList();
    public JFXTextField returnAmount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        totalAmountField.setText("" + totalAmount);

        paidAmountField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.isEmpty()) {
                    Platform.runLater(() -> {
                        if (!newValue.matches("[0-9]+")) {
                            JFXUtil.showAlertBox("Please input only Number Value");

                        } else {
                            double paid = Double.parseDouble(newValue);
                            if(paid > totalAmount){
                                double returnValue = paid - totalAmount;
                                returnAmount.setText("" + returnValue);
                            }
                        }
                    });
                }
                else {
                    returnAmount.setText("0");
                }
            }
        });
    }

    public void confirmAction(ActionEvent actionEvent) {
        onCheckoutEntry();
    }

    private void onCheckoutEntry() {
        String id = DatabaseUtil.firebaseDatabase.push().getKey();
        String currentMonth = JFXUtil.getCurrentMonth();
        String currentDate = JFXUtil.getCurrentDate();

        CheckoutModel checkoutModel = new CheckoutModel(Products, totalAmount);

        DatabaseUtil.firebaseDatabase.child(AppConstant.INVOICES_DATABASE_NODE_NAME)
                .child(currentMonth)
                .child(currentDate)
                .child(AppConstant.SELL_DATABASE_NODE_NAME)
                .child(id)
                .setValue(checkoutModel, (databaseError, databaseReference) -> {
                    for (SellProductModel sell : Products){
                        updateStockItem(sell.getId(), sell.getQuantity());
                    }
                    SellController.productModels.clear();
                });

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful");
        alert.setHeaderText("Product Sold Successfully");
        alert.show();
    }

    private void updateStockItem(String productId, int productQuantity){

        DatabaseUtil.getDataForSingleValueEvent(AppConstant.STOCK_DATABASE_NODE_NAME, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                int inStock = productQuantity;

                if(dataSnapshot.child(productId).exists()){
                    int currentStock = Integer.parseInt(dataSnapshot.child(productId).child("inStock").getValue().toString());
                    inStock = currentStock - inStock;
                }

                Stock stock = new Stock(String.valueOf(inStock));

                DatabaseUtil.firebaseDatabase.child(AppConstant.STOCK_DATABASE_NODE_NAME)
                        .child(productId)
                        .setValue(stock, (databaseError, databaseReference) -> {
                        });
            }

            @Override
            public void onFailure(DatabaseError databaseError) {

            }
        });
    }

    public void closeAction(ActionEvent actionEvent) {
        ((Stage) printButton.getScene().getWindow()).close();
    }

    public void printAction(ActionEvent actionEvent) {

    }
}
