package com.nerdgeeks.shop.Controller.Supplier;

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

import java.net.URL;
import java.util.ResourceBundle;

public class EditSupplierController implements Initializable{


    public JFXTextField editSupplierNameField;
    public JFXTextField editSupplierContactField;
    public JFXTextField editSupplierAddressField;
    public JFXTextField editSupplierDescriptionField;
    public JFXButton updateButton;

    private String supplier_Name,supplier_Contact,supplier_Address,supplier_Des;

    public static Supplier editSupplier;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        editSupplierNameField.setText(editSupplier.getSupplierName());
        editSupplierContactField.setText(editSupplier.getSupplierNo());
        editSupplierAddressField.setText(editSupplier.getSupplierAddress());
        editSupplierDescriptionField.setText(editSupplier.getSupplierDescription());
    }

    @FXML
    public void closeAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    public void updateButtonAction(ActionEvent actionEvent) {
        updateData();
    }

    private void updateData(){
        if (validateInput()) {

            supplier_Name = editSupplierNameField.getText();
            supplier_Contact = editSupplierContactField.getText();
            supplier_Address = editSupplierAddressField.getText();
            supplier_Des = editSupplierDescriptionField.getText();

            String supplierId = editSupplier.getSupplierId();

            Supplier supplier = new Supplier(supplierId,supplier_Name,supplier_Contact,supplier_Address,supplier_Des);

            DatabaseUtil.firebaseDatabase.child(AppConstant.SUPPLIERS_DATABASE_NODE_NAME).child(supplierId)
                    .setValue(supplier, (databaseError, databaseReference) -> {

            });

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText("Supplier details updated Successfully");
            alert.show();

            ((Stage) updateButton.getScene().getWindow()).close();
        }
    }

    public void cancelButtonAction(ActionEvent actionEvent) {
        editSupplierNameField.setText(editSupplier.getSupplierName());
        editSupplierContactField.setText(editSupplier.getSupplierNo());
        editSupplierAddressField.setText(editSupplier.getSupplierAddress());
        editSupplierDescriptionField.setText(editSupplier.getSupplierDescription());
    }

    private boolean validateInput() {

        String errorMessage = "";

        if (editSupplierNameField.getText().isEmpty()) {
            errorMessage += "Name Required!\n";
        }
        if (editSupplierContactField.getText().isEmpty()) {
            errorMessage += "Phone number Required!\n";
        }
        if (editSupplierAddressField.getText().isEmpty()) {
            errorMessage += "Address Required!\n";
        }
        if (editSupplierDescriptionField.getText().isEmpty()) {
            errorMessage += "Description Required!\n";
        }

        if (errorMessage.length() == 0) {
            return true;

        } else {
            JFXUtil.showAlertBox(errorMessage);
            return false;
        }
    }

}
