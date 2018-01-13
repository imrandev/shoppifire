package com.nerdgeeks.shop.Controller.Purchase;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.nerdgeeks.shop.Model.DynamicField;
import com.nerdgeeks.shop.Model.Purchase;
import com.nerdgeeks.shop.Model.PurchaseProduct;
import com.nerdgeeks.shop.Util.AppConstant;
import com.nerdgeeks.shop.Util.DatabaseUtil;
import com.nerdgeeks.shop.Util.JFXUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class PurchaseProductController implements Initializable {

    public GridPane gridPane;
    public JFXTextField quantityField;
    public JFXTextField totalBuyingPriceField;
    public JFXComboBox productNameField;
    public JFXComboBox productSupplierField;
    public JFXTextField paidField;
    public JFXButton saveButton;
    public VBox vBox;
    public ScrollPane scrollPane;
    public Button add;
    public Button del;

    private ObservableList<DynamicField> dynamicFields = FXCollections.observableArrayList();

    private int row = 2;
    private int col = 0;
    private int rowHeight = 50;
    private int finalPrice = 0;

    private ObservableList supplierProductName = FXCollections.observableArrayList();
    private ObservableList supplierProductId = FXCollections.observableArrayList();
    private ObservableList supplierProductBuyingPrice = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        productSupplierField.setItems(DatabaseUtil.getSupplierName());
        scrollPane.vvalueProperty().bind(gridPane.heightProperty());
        quantityField.setDisable(true);
        totalBuyingPriceField.setEditable(false);
        dynamicFields.add(new DynamicField(productNameField, quantityField));

        resetButtonAction();

        productSupplierField.valueProperty().addListener((observable, oldValue, newValue) -> {
            supplierProductName = DatabaseUtil.getProductDetailsForSupplier(newValue.toString(), "productName");
            supplierProductId = DatabaseUtil.getProductDetailsForSupplier(newValue.toString(), "productId");
            supplierProductBuyingPrice = DatabaseUtil.getProductDetailsForSupplier(newValue.toString(), "productBuyingPrice");
            setProductNameForAllCombobox();
        });

        paidField.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.isEmpty()) {

                Platform.runLater(() -> {
                    if (!newValue.matches("[0-9]+")) {
                        JFXUtil.showAlertBox("Please input only Number Value");
                        paidField.setText("");
                    } else {
                        if (Integer.parseInt(newValue) > finalPrice) {
                            JFXUtil.showAlertBox("Paid Value More then total value");
                            paidField.setText("");
                        }
                    }
                });
            }
        });
    }

    private void setProductNameForAllCombobox() {

        for (DynamicField dynamicField : dynamicFields) {

            dynamicField.getJfxComboBox().setItems(supplierProductName);

            if (dynamicField.getJfxTextField().getText().isEmpty()) {
                dynamicField.getJfxTextField().setDisable(true);
            }

            dynamicField.getJfxComboBox().valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    dynamicField.getJfxTextField().setDisable(false);
                    totalPriceCalculation();
                }
            });

            dynamicField.getJfxTextField().textProperty().addListener((observable, oldValue, newValue) -> {

                if (newValue.matches("[0-9]+") || newValue.isEmpty()) {
                    totalPriceCalculation();
                } else {
                    JFXUtil.showAlertBox("Please input only Number Value");
                    dynamicField.getJfxTextField().setText("");
                }
            });
        }
    }

    private void totalPriceCalculation() {

        finalPrice = 0;

        for (DynamicField dynamicField : dynamicFields) {

            if (!dynamicField.getJfxTextField().getText().isEmpty()) {
                String productBuyingPrice = supplierProductBuyingPrice.get(dynamicField.getJfxComboBox().getSelectionModel().getSelectedIndex()).toString();
                int quantity = Integer.parseInt(dynamicField.getJfxTextField().getText());
                int price = Integer.parseInt(productBuyingPrice) * quantity;
                finalPrice += price;
            }
        }
        totalBuyingPriceField.setText(String.valueOf(finalPrice));
    }

    @FXML
    public void closeAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    public void saveButtonAction(ActionEvent actionEvent) {

        if (validateInput()) {

            String supplier = productSupplierField.getSelectionModel().getSelectedItem().toString();
            String totalBuyingPrice = String.valueOf(finalPrice);
            String totalPaid = paidField.getText();
            String totalDue = String.valueOf(Integer.valueOf(totalBuyingPrice) - Integer.valueOf(totalPaid));
            String currentMonth = JFXUtil.getCurrentMonth();
            String currentDate = JFXUtil.getCurrentDate();
            String purchaseConfirm = "false";
            String id = DatabaseUtil.firebaseDatabase.push().getKey();

            Purchase Purchase = new Purchase(id, supplier, totalBuyingPrice, totalPaid, totalDue, currentMonth, currentDate, purchaseConfirm);

            DatabaseUtil.firebaseDatabase.child(AppConstant.INVOICES_DATABASE_NODE_NAME)
                    .child(currentMonth)
                    .child(currentDate)
                    .child(AppConstant.PURCHASE_DATABASE_NODE_NAME)
                    .child(supplier)
                    .child(id)
                    .setValue(Purchase, (databaseError, databaseReference) -> {

                        for (DynamicField dynamicField : dynamicFields) {

                            String singleProductName = dynamicField.getJfxComboBox().getSelectionModel().getSelectedItem().toString();
                            String singleProductId = supplierProductId.get(dynamicField.getJfxComboBox().getSelectionModel().getSelectedIndex()).toString();
                            String productQuantity = dynamicField.getJfxTextField().getText();
                            PurchaseProduct purchaseProduct = new PurchaseProduct(singleProductId, singleProductName, productQuantity);

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
    public void resetButtonAction() {

        totalBuyingPriceField.setText("0");
        paidField.clear();

        for (DynamicField dynamicField : dynamicFields) {
            dynamicField.getJfxComboBox().getSelectionModel().clearSelection();
            dynamicField.getJfxTextField().setText("");
            dynamicField.getJfxTextField().setDisable(true);
        }
    }

    @FXML
    public void addAction(ActionEvent actionEvent) {

        createNewProductInputTextField(actionEvent);
    }

    @FXML
    public void delAction(ActionEvent actionEvent) {

        deleteProductInputTextField(actionEvent);
        totalPriceCalculation();
    }

    private void createNewProductInputTextField(ActionEvent actionEvent) {

        makeNewRow();

        JFXTextField jfxTextField = new JFXTextField();
        jfxTextField.setLabelFloat(true);
        jfxTextField.setPromptText("Quantity" + row);
        jfxTextField.setMaxSize(100, 35);
        jfxTextField.setAlignment(Pos.TOP_LEFT);
        GridPane.setMargin(jfxTextField, new Insets(15, 0, 5, 0));

        JFXComboBox jfxComboBox = new JFXComboBox();
        jfxComboBox.setLabelFloat(true);
        GridPane.setColumnSpan(jfxComboBox, 2);
        jfxComboBox.setMaxSize(210, 35);
        jfxComboBox.setPromptText("Product Name" + row);
        GridPane.setMargin(jfxComboBox, new Insets(15, 0, 5, 0));

        RowConstraints rc1 = new RowConstraints();
        rc1.setVgrow(Priority.ALWAYS);
        rc1.setMaxHeight(rowHeight);

        gridPane.getRowConstraints().add(row, rc1);
        gridPane.setVgap(15);
        gridPane.setHgap(5);
        gridPane.add(jfxComboBox, col, row);
        gridPane.add(jfxTextField, col + 1, row);
        row++;

        DynamicField dynamicField = new DynamicField(jfxComboBox, jfxTextField); // make object of two field
        dynamicFields.add(dynamicField);

        setProductNameForAllCombobox();

        Stage ne = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        if (ne.getHeight() < 500) {
            ne.setHeight(ne.getHeight() + (rowHeight + 15));
        }
    }

    private void deleteProductInputTextField(ActionEvent actionEvent) {

        if (row > 2) {
            deleteRow(gridPane, row - 1);
            gridPane.getRowConstraints().remove(gridPane.getRowCount() - 2);
            row--;
            if (dynamicFields.size() > 1) {
                dynamicFields.remove(dynamicFields.size() - 1);
            }

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            if (gridPane.getHeight() < stage.getHeight() && stage.getHeight() > 400) {
                stage.setHeight(stage.getHeight() - (rowHeight + 15));
            }
        }
    }

    private void makeNewRow() {
        gridPane.setRowIndex(totalBuyingPriceField, gridPane.getRowIndex(totalBuyingPriceField) + 1);
        gridPane.setRowIndex(paidField, gridPane.getRowIndex(paidField) + 1);
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
                GridPane.setRowIndex(child, r - 1);
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
        if (finalPrice < 0) {
            errorMessage += "Please Check the Total Buying Price!!\n";
        }
        if (paidField.getText().isEmpty()) {
            errorMessage += "Please input the paid field!!\n";
        }

        for (DynamicField dynamicField : dynamicFields) {

            if (dynamicField.getJfxTextField().getText().isEmpty() || dynamicField.getJfxComboBox().getSelectionModel().isEmpty()) {
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

