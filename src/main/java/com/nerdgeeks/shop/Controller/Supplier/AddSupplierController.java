package com.nerdgeeks.shop.Controller.Supplier;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.nerdgeeks.shop.Model.Supplier;
import com.nerdgeeks.shop.Util.AppConstant;
import com.nerdgeeks.shop.Util.DatabaseUtil;
import com.nerdgeeks.shop.Util.JFXUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AddSupplierController implements Initializable{


    public JFXTextField supplierNameField;
    public JFXTextField supplierContactField;
    public JFXTextField supplierAddressField;
    public JFXTextField supplierDescriptionField;
    public JFXButton saveButton;

    private String supplier_Name,supplier_Contact,supplier_Address,supplier_Des;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //saveButton.setDisable(true);

    }

    @FXML
    public void closeAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    public void saveButtonAction(ActionEvent actionEvent) {

        if (validateInput()) {

            supplier_Name = supplierNameField.getText();
            supplier_Contact = supplierContactField.getText();
            supplier_Address = supplierAddressField.getText();
            supplier_Des = supplierDescriptionField.getText();

            String supplierId = DatabaseUtil.firebaseDatabase.push().getKey();

            Supplier supplier = new Supplier(supplierId,supplier_Name,supplier_Contact,supplier_Address,supplier_Des);

            DatabaseUtil.firebaseDatabase.child(AppConstant.SUPPLIERS_DATABASE_NODE_NAME).child(supplierId).setValue(supplier, (databaseError, databaseReference) -> {

            });

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText("Supplier added Successfully");
            alert.show();

            ((Stage) saveButton.getScene().getWindow()).close();

        }
    }

    public void cancelButtonAction(ActionEvent actionEvent) {
        supplierNameField.clear();
        supplierContactField.clear();
        supplierAddressField.clear();
        supplierDescriptionField.clear();
    }

    private boolean validateInput() {

        String errorMessage = "";

        if (supplierNameField.getText().isEmpty()) {
            errorMessage += "Name Required!!\n";
        }
        if (supplierContactField.getText().isEmpty()) {
            errorMessage += "Phone number Required!!\n";
        }
        if (supplierAddressField.getText().isEmpty()) {
            errorMessage += "Address Required!!\n";
        }
        if (supplierDescriptionField.getText().isEmpty()) {
            errorMessage += "Description Required!!\n";
        }

        if (errorMessage.length() == 0) {
            return true;

        } else {
            JFXUtil.showAlertBox(errorMessage);
            return false;
        }
    }

}
