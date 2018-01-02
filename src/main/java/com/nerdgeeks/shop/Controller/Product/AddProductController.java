package com.nerdgeeks.shop.Controller.Product;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.nerdgeeks.shop.Model.Product;
import com.nerdgeeks.shop.Util.AppConstant;
import com.nerdgeeks.shop.Util.AutoCompleteComboBoxListener;
import com.nerdgeeks.shop.Util.DatabaseUtil;
import com.nerdgeeks.shop.Util.JFXUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class AddProductController implements Initializable{

    public JFXButton saveButton;
    public JFXTextField productNameField;
    public JFXTextField productIdField;
    public JFXTextField buyingPriceField;
    public JFXTextField sellingPriceField;
    public JFXTextField productVatField;
    public JFXComboBox productCategoryField;
    public JFXComboBox productSupplierField;

    static ObservableList<String> category = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        productCategoryField.setItems(category);
        productSupplierField.setItems(DatabaseUtil.getSupplierName());

        new AutoCompleteComboBoxListener<>(productCategoryField);

        buyingPriceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]+") && !newValue.isEmpty()) {
                JFXUtil.showAlertBox("Please input only Number Value");
                buyingPriceField.setText("");
            }
        });

        sellingPriceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]+") && !newValue.isEmpty()) {
                JFXUtil.showAlertBox("Please input only Number Value");
                sellingPriceField.setText("");
            }
        });
    }



    @FXML
    public void closeAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    public void saveButtonAction(ActionEvent actionEvent) {

        if (validateInput()) {

            String product_Name = productNameField.getText();
            String buying_Price = buyingPriceField.getText();
            String selling_Price = sellingPriceField.getText();
            String product_Vat = productVatField.getText();
            String product_Category = productCategoryField.getSelectionModel().getSelectedItem().toString();
            String product_Supplier = productSupplierField.getSelectionModel().getSelectedItem().toString();
            String productId = productIdField.getText();

            Product product = new Product(productId,product_Name,
                    buying_Price,selling_Price,product_Vat,product_Category,product_Supplier);

            DatabaseUtil.firebaseDatabase.child(AppConstant.PRODUCTS_DATABASE_NODE_NAME)
                    .child(product_Supplier)
                    .child(product_Category)
                    .child(productId)
                    .setValue(product, (databaseError, databaseReference) -> { });

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText("Product added Successfully");
            alert.show();

            ((Stage) saveButton.getScene().getWindow()).close();

        }
    }

    public void resetButtonAction(ActionEvent actionEvent) {
        productIdField.setText("");
        productNameField.setText("");
        buyingPriceField.setText("");
        sellingPriceField.setText("");
        productVatField.setText("");
        productCategoryField.getSelectionModel().select(null);
        productSupplierField.getSelectionModel().clearSelection();
    }

    private boolean validateInput() {

        String errorMessage = "";

        if (productNameField.getText().isEmpty()) {
            errorMessage += "Product Name Required!!\n";
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
        if (productVatField.getText().isEmpty()) {
            errorMessage += "Vat Required!!\n";
        }
        if (productCategoryField.getSelectionModel().isEmpty()) {
            errorMessage += "Category Required!!\n";
        }
        if (productSupplierField.getSelectionModel().isEmpty()) {
            errorMessage += "Supplier Required!!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            JFXUtil.showAlertBox(errorMessage);
            return false;
        }
    }

}
