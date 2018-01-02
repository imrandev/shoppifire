package com.nerdgeeks.shop.Controller.Purchase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jfoenix.controls.JFXButton;
import com.nerdgeeks.shop.Model.PurchaseProduct;
import com.nerdgeeks.shop.Util.AppConstant;
import com.nerdgeeks.shop.Util.DatabaseUtil;
import com.nerdgeeks.shop.Util.JFXUtil;
import com.nerdgeeks.shop.Util.OnGetDataListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewPurchasedProductController implements Initializable{

    public TableView viewPurchasedTable;
    public JFXButton confirmButton;
    private ObservableList<PurchaseProduct> purchaseProductsData = FXCollections.observableArrayList();
    public static DatabaseReference purchaseProductDatabaseRef;
    private String purchaseConfirm;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        viewPurchasedTable.getColumns().clear();
        String[] colName = {"productName","productQuantity"};

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
                   confirmButton.setText("All Product's Already Received.");
               } else {
                   confirmButton.setDisable(false);
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

    }

    public void productReceivedConfirmButtonAction(ActionEvent actionEvent) {

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
                    int currentStock = Integer.parseInt(dataSnapshot.child(productId).child("InStock").getValue().toString());
                    inStock += currentStock;
                }

                DatabaseUtil.firebaseDatabase.child(AppConstant.STOCK_DATABASE_NODE_NAME)
                        .child(productId)
                        .child("InStock")
                        .setValue(inStock, (databaseError, databaseReference) -> {

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
    public void closeAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
