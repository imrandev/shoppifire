package com.nerdgeeks.shop.Controller.Purchase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.nerdgeeks.shop.Model.Purchase;
import com.nerdgeeks.shop.Model.PurchaseProduct;
import com.nerdgeeks.shop.Model.Stock;
import com.nerdgeeks.shop.Util.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewPurchasedProductController implements Initializable{

    public TableView viewPurchasedTable;
    public JFXButton confirmButton, updateDueBtn;
    public JFXTextField totalDueField, paidField;
    private ObservableList<PurchaseProduct> purchaseProductsData = FXCollections.observableArrayList();
    public static DatabaseReference purchaseProductDatabaseRef;
    public static Purchase selectedPurchaseData;
    private String purchaseConfirm;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        viewPurchasedTable.getColumns().clear();
        String[] colName = {"productName","productQuantity"};
        totalDueField.setEditable(false);
        updateDueBtn.setDisable(true);

        String totalDue = selectedPurchaseData.getTotalDue();
        if (totalDue.isEmpty() || totalDue.equals("0")){
            paidField.setDisable(true);
        } else {
            totalDueField.setText(selectedPurchaseData.getTotalDue());
        }

        paidField.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.isEmpty()) {

                Platform.runLater(() -> {
                    if (!newValue.matches("[0-9]+")) {
                        JFXUtil.showAlertBox("Please input only Number Value");
                        paidField.setText("");
                    }
                    if(Integer.parseInt(newValue) > Integer.parseInt(selectedPurchaseData.getTotalDue())){
                        JFXUtil.showAlertBox("Paid Value More then total due value");
                        paidField.setText("");
                    }
                    updateDueBtn.setDisable(false);
                });
            } else {
                updateDueBtn.setDisable(true);
            }

        });

        purchaseProductDatabaseRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

               if (purchaseProductsData.size() > 0) {
                   purchaseProductsData.clear();
               }

               purchaseConfirm = dataSnapshot.child("purchaseConfirm").getValue().toString();

               for (DataSnapshot products: dataSnapshot.child("Products").getChildren()){
                   PurchaseProduct purchaseProduct = products.getValue(PurchaseProduct.class);
                   purchaseProductsData.add(purchaseProduct);
               }
               JFXUtil.setTableData(viewPurchasedTable,colName,purchaseProductsData);

               if (purchaseConfirm.equals("true")){
                   confirmButton.setDisable(true);
                   confirmButton.setText("Product's Already Received");
               } else {
                   confirmButton.setDisable(false);
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

    }

    @FXML
    public void productReceivedConfirmButtonAction() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you received the product ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            for (PurchaseProduct purchaseProduct: purchaseProductsData){
                updateStockItem(purchaseProduct.getProductId(), purchaseProduct.getProductQuantity());
            }
        }
    }


    private void updateStockItem(String productId, String productQuantity){

        DatabaseUtil.getDataForSingleValueEvent(AppConstant.STOCK_DATABASE_NODE_NAME, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                int inStock = Integer.parseInt(productQuantity);

                if(dataSnapshot.child(productId).exists()){
                    int currentStock = Integer.parseInt(dataSnapshot.child(productId).child("inStock").getValue().toString());
                    inStock += currentStock;
                }

                Stock stock = new Stock(String.valueOf(inStock));

                DatabaseUtil.firebaseDatabase.child(AppConstant.STOCK_DATABASE_NODE_NAME)
                        .child(productId)
                        .setValue(stock, (databaseError, databaseReference) -> {
                            // Set purchase confirm false to true
                            purchaseProductDatabaseRef.child("purchaseConfirm").setValue("true", (databaseError1, databaseReference1) -> {

                            });
                        });
            }

            @Override
            public void onFailure(DatabaseError databaseError) {

            }
        });
    }

    @FXML
    public void actionUpdateDue() {

        String updatePaidValue = String.valueOf(Integer.valueOf(selectedPurchaseData.getTotalPaid())
                +Integer.valueOf(paidField.getText()));
        String updateDueVale = String.valueOf(Integer.valueOf(selectedPurchaseData.getTotalBuyingPrice())
                -Integer.valueOf(updatePaidValue));

        purchaseProductDatabaseRef.child("totalPaid").setValue(updatePaidValue, (databaseError1, databaseReference1) -> {

        });

        purchaseProductDatabaseRef.child("totalDue").setValue(updateDueVale, (databaseError1, databaseReference1) -> {

        });

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful");
        alert.setHeaderText("Paid Successfully");
        alert.show();

        ((Stage) updateDueBtn.getScene().getWindow()).close();

    }

    @FXML
    public void closeAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
