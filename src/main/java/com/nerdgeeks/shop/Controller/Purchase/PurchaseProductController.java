package com.nerdgeeks.shop.Controller.Purchase;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.nerdgeeks.shop.Model.DynamicField;
import com.nerdgeeks.shop.Model.Product;
import com.nerdgeeks.shop.Model.Purchase;
import com.nerdgeeks.shop.Model.PurchaseProduct;
import com.nerdgeeks.shop.Util.AppConstant;
import com.nerdgeeks.shop.Util.DatabaseUtil;
import com.nerdgeeks.shop.Util.JFXUtil;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class PurchaseProductController implements Initializable{

    public GridPane gridPane;
    public JFXTextField quantityField;
    public JFXTextField totalByuingPriceField;
    public JFXComboBox productNameField;
    public JFXComboBox productSupplierField;
    public JFXTextField paidField;
    public JFXButton saveButton;
    public VBox vBox;
    public ScrollPane scrollPane;

    private ObservableList<DynamicField> dynamicFields = FXCollections.observableArrayList();

    private int  row = 2;
    private int col = 0;
    private int rowHeight = 50;
    private int[] finalPrice;

    private ObservableList supplierProductName = FXCollections.observableArrayList();
    private ObservableList supplierProductId = FXCollections.observableArrayList();
    private ObservableList supplierProductBuyingPrice = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        productSupplierField.setItems(DatabaseUtil.getSupplierName());
        totalByuingPriceField.setText("0");

        scrollPane.vvalueProperty().bind(gridPane.heightProperty());

        dynamicFields.add(new DynamicField(productNameField,quantityField));

        productSupplierField.valueProperty().addListener((observable, oldValue, newValue) -> {

            supplierProductName = DatabaseUtil.getProductNameForSupplier(newValue.toString());
            supplierProductId = DatabaseUtil.getProductIdForSupplier(newValue.toString());
            supplierProductBuyingPrice = DatabaseUtil.getProductBuyingPriceForSupplier(newValue.toString());

            setProductNameForAllCombobox();
        });

    }

    private void setProductNameForAllCombobox(){
        for (DynamicField dynamicField: dynamicFields) {
            dynamicField.getJfxComboBox().setItems(supplierProductName);

            dynamicField.getJfxTextField().textProperty().addListener((observable, oldValue, newValue) -> {

                int quantity;
                int totalPrice = Integer.parseInt(totalByuingPriceField.getText());
                if(newValue.equals("")){
                    if(dynamicFields.size()>1){
                        totalPrice = totalPrice-finalPrice[dynamicFields.size()];
                    } else {
                        totalPrice = 0;
                    }
                } else {
                    quantity = Integer.parseInt(newValue);
                    String productPrice = supplierProductBuyingPrice.get(dynamicField.getJfxComboBox().getSelectionModel().getSelectedIndex()).toString();
                    totalPrice = totalPrice + (Integer.parseInt(productPrice) * quantity);
                }
                totalByuingPriceField.setText(String.valueOf(totalPrice));
                finalPrice[dynamicFields.size()-1] = totalPrice;
            });
        }
    }

    @FXML
    public void closeAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    public void saveButtonAction(ActionEvent actionEvent) {

        if (validateInput()) {

            String supplier = productSupplierField.getSelectionModel().getSelectedItem().toString();
            String totalBuyingPrice = totalByuingPriceField.getText();
            String totalPaid = paidField.getText();
            String currentMonth = JFXUtil.getCurrentMonth();
            String currentDate = JFXUtil.getCurrentDate();
            String id = DatabaseUtil.firebaseDatabase.push().getKey();

            Purchase supply = new Purchase(id, supplier, totalBuyingPrice, totalPaid, currentMonth,currentDate);

            DatabaseUtil.firebaseDatabase.child(AppConstant.INVOICES_DATABASE_NODE_NAME)
                    .child(currentMonth)
                    .child(currentDate)
                    .child(AppConstant.PURCHASE_DATABASE_NODE_NAME)
                    .child(supplier)
                    .child(id)
                    .setValue(supply, (databaseError, databaseReference) -> {

                        for (DynamicField dynamicField: dynamicFields){

                            String singleProductName = dynamicField.getJfxComboBox().getSelectionModel().getSelectedItem().toString();
                            String singleProductId = supplierProductId.get(dynamicField.getJfxComboBox().getSelectionModel().getSelectedIndex()).toString();
                            String productQuantity = dynamicField.getJfxTextField().getText();
                            PurchaseProduct purchaseProduct = new PurchaseProduct(singleProductName, productQuantity);

                            databaseReference.child(AppConstant.PRODUCTS_DATABASE_NODE_NAME)
                                    .child(singleProductId)
                                    .setValue(purchaseProduct, (databaseError1, databaseReference1) -> {
                                    });
                        }
                    });

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText("Product Purchase Successfully");
            alert.show();

            ((Stage) saveButton.getScene().getWindow()).close();

        }
    }

    @FXML
    public void resetButtonAction(ActionEvent actionEvent) {

        totalByuingPriceField.setText("0");
        paidField.clear();

        for (DynamicField dynamicField: dynamicFields) {
            dynamicField.getJfxComboBox().getSelectionModel().clearSelection();
            dynamicField.getJfxTextField().setText("");
        }
    }

    @FXML
    public void addAction(ActionEvent actionEvent) {

        createNewProductInputTextField(actionEvent);
    }

    @FXML
    public void delAction(ActionEvent actionEvent) {

        deleteProductInputTextField(actionEvent);
    }

    private void createNewProductInputTextField(ActionEvent actionEvent){

        makeNewRow();

        JFXTextField jfxTextField =  new JFXTextField();
        jfxTextField.setLabelFloat(true);
        jfxTextField.setPromptText("Quantity"+row);
        jfxTextField.setMaxSize(100,35);
        jfxTextField.setAlignment(Pos.TOP_LEFT);
        GridPane.setMargin(jfxTextField, new Insets(15, 0, 0, 0));

        JFXComboBox jfxComboBox = new JFXComboBox();
        jfxComboBox.setLabelFloat(true);
        GridPane.setColumnSpan(jfxComboBox,2);
        jfxComboBox.setMaxSize(230,35);
        jfxComboBox.setPromptText("Product Name"+row);
        GridPane.setMargin(jfxComboBox, new Insets(15, 0, 0, 0));

        RowConstraints rc1 = new RowConstraints();
        rc1.setVgrow(Priority.ALWAYS);
        rc1.setMaxHeight(rowHeight);

        gridPane.getRowConstraints().add(row,rc1);
        gridPane.setVgap(15);
        gridPane.add(jfxComboBox,col,row);
        gridPane.add(jfxTextField,col+1,row);
        row++;

        DynamicField dynamicField = new DynamicField(jfxComboBox,jfxTextField); // make object of two field
        dynamicFields.add(dynamicField);

        setProductNameForAllCombobox();

        Stage ne = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        if(ne.getHeight()<550) {
            ne.setHeight(ne.getHeight() + (rowHeight + 15));
        }
    }

    private void deleteProductInputTextField(ActionEvent actionEvent){

        if(row>2){
            deleteRow(gridPane,row-1);
            gridPane.getRowConstraints().remove(gridPane.getRowCount()-2);
            row--;
            if(dynamicFields.size() > 1){
                dynamicFields.remove(dynamicFields.size()-1);
            }

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            if(gridPane.getHeight() < stage.getHeight() && stage.getHeight()>400) {
                stage.setHeight(stage.getHeight() - (rowHeight + 15));
            }
        }
    }

    private void makeNewRow(){
        gridPane.setRowIndex(totalByuingPriceField,gridPane.getRowIndex(totalByuingPriceField)+1);
        gridPane.setRowIndex(paidField,gridPane.getRowIndex(paidField)+1);
    }

    static void deleteRow(GridPane grid, final int row) {
        Set<Node> deleteNodes = new HashSet<>();
        for (Node child : grid.getChildren()) {
            // get index from child
            Integer rowIndex = GridPane.getRowIndex(child);

            // handle null values for index=0
            int r = rowIndex == null ? 0 : rowIndex;

            if (r > row) {
                // decrement rows for rows after the deleted row
                GridPane.setRowIndex(child, r-1);
            } else if (r == row) {
                // collect matching rows for deletion
                deleteNodes.add(child);
            }
        }
        // remove nodes from row
        grid.getChildren().removeAll(deleteNodes);
    }

    private boolean validateInput() {

        String errorMessage = "";

        if (productSupplierField.getSelectionModel().isEmpty()) {
            errorMessage += "Please Select Supplier!!\n";
        }
        if (totalByuingPriceField.getText().isEmpty()) {
            errorMessage += "Please input the buying price!!\n";
        }
        if (paidField.getText().isEmpty()) {
            errorMessage += "Please input the paid field!!\n";
        }

        for (DynamicField dynamicField: dynamicFields) {

            if(dynamicField.getJfxTextField().getText().isEmpty() || dynamicField.getJfxComboBox().getSelectionModel().isEmpty()){
                errorMessage += "Product name and quantity required!!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            JFXUtil.showAlertBox(errorMessage);
            return false;
        }
    }
}

