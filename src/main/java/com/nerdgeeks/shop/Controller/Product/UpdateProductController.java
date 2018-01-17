package com.nerdgeeks.shop.Controller.Product;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.nerdgeeks.shop.Model.Product;
import com.nerdgeeks.shop.Util.AppConstant;
import com.nerdgeeks.shop.Util.DatabaseUtil;
import com.nerdgeeks.shop.Util.JFXUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateProductController implements Initializable{

    public JFXButton updateButton;
    public JFXTextField productNameField;
    public JFXTextField productIdField;
    public JFXTextField buyingPriceField;
    public JFXTextField sellingPriceField;
    public JFXComboBox productCategoryField;

    static ObservableList<String> category = FXCollections.observableArrayList();
    static Product updateProduct;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        productCategoryField.setItems(category);

        productIdField.setEditable(false);

        productCategoryField.getSelectionModel().select(updateProduct.getProductCategory());
        productNameField.setText(updateProduct.getProductName());
        productIdField.setText(updateProduct.getProductId());
        buyingPriceField.setText(updateProduct.getBuyingPrice());
        sellingPriceField.setText(updateProduct.getSellingPrice());
    }

    @FXML
    public void updateButtonAction(ActionEvent actionEvent) {

        if (validateInput()) {

            String product_Name = productNameField.getText();
            String product_Id = productIdField.getText();
            String buying_Price = buyingPriceField.getText();
            String selling_Price = sellingPriceField.getText();
            String product_Category = productCategoryField.getSelectionModel().getSelectedItem().toString();
            String product_Supplier = updateProduct.getProductSupplier();

            Product product = new Product(product_Id,product_Name, buying_Price,selling_Price,product_Category,product_Supplier);

            DatabaseUtil.firebaseDatabase.child(AppConstant.PRODUCTS_DATABASE_NODE_NAME)
                    .child(product_Supplier)
                    .child(product_Category)
                    .child(product_Id)
                    .setValue(product, (databaseError, databaseReference) -> { });

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText("Product details updated Successfully");
            alert.show();

            ((Stage) updateButton.getScene().getWindow()).close();

        }
    }

    @FXML
    public void resetButtonAction(ActionEvent actionEvent) {
        productNameField.clear();
        productIdField.clear();
        buyingPriceField.clear();
        sellingPriceField.clear();
        productCategoryField.getSelectionModel().clearSelection();
    }

    private boolean validateInput() {

        String errorMessage = "";

        if (productNameField.getText().isEmpty()) {
            errorMessage += "Product Name Required!\n";
        }
        if (productIdField.getText().isEmpty()) {
            errorMessage += "Product ID Required!!\n";
        }
        if (buyingPriceField.getText().isEmpty()) {
            errorMessage += "buying Price Required!!\n";
        }
        if (sellingPriceField.getText().isEmpty()) {
            errorMessage += "Selling Price Required!!\n";
        }
        if (productCategoryField.getSelectionModel().isEmpty()) {
            errorMessage += "Category Required!!\n";
        }

        if (errorMessage.length() == 0) {
            return true;

        } else {
            JFXUtil.showAlertBox(errorMessage);
            return false;
        }
    }

    @FXML
    public void closeAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

}
