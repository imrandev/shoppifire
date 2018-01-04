package com.nerdgeeks.shop.Controller.Purchase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.nerdgeeks.shop.Model.Purchase;
import com.nerdgeeks.shop.Util.AppConstant;
import com.nerdgeeks.shop.Util.DatabaseUtil;
import com.nerdgeeks.shop.Util.JFXUtil;
import com.nerdgeeks.shop.Util.OnGetDataListener;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.net.URL;
import java.util.ResourceBundle;

public class PurchaseController implements Initializable {

    public TableView<Purchase> purchaseProductTable;
    public JFXButton addButton, deleteButton;
    public VBox purchaseProductPane;
    public JFXComboBox selectMonthField;
    public JFXComboBox selectDateField;
    public JFXButton resetButton;

    private ObservableList<Purchase> purchaseData = FXCollections.observableArrayList();
    private ObservableList<DatabaseReference> purchaseProductsDatabaseRef = FXCollections.observableArrayList();

    private ObservableList<String> selectMonth = FXCollections.observableArrayList();
    private ObservableList<String> selectDate = FXCollections.observableArrayList();

    private String currentMonth = JFXUtil.getCurrentMonth();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setComboBoxData();

        resetButton.setDisable(true);

        deleteButton.disableProperty().bind(Bindings.isEmpty(purchaseProductTable.getSelectionModel().getSelectedItems()));

        purchaseProductPane.setOnMouseClicked(event -> purchaseProductTable.getSelectionModel().clearSelection());

        selectMonthField.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                currentMonth = newValue.toString();
                setTableColumnDataForPurchase(null);
            }
        });

        selectDateField.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                resetButton.setDisable(false);
                setTableColumnDataForPurchase(newValue.toString());
            } else {
                resetButton.setDisable(true);
            }
        });
    }

    private void setTableColumnDataForPurchase(String date){

        //Firebase Databse get data and set data to table
        DatabaseUtil.getDataValueEvent(AppConstant.INVOICES_DATABASE_NODE_NAME, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                if(purchaseData.size()>0){
                    purchaseData.clear();
                }

                if (purchaseProductsDatabaseRef.size()>0){
                    purchaseProductsDatabaseRef.clear();
                }

                if (date!=null){
                    for (DataSnapshot snapshot : dataSnapshot.child(currentMonth).child(date).child(AppConstant.PURCHASE_DATABASE_NODE_NAME).getChildren()){
                        getPurchaseRecord(snapshot);
                    }
                } else {
                    selectDate.clear();
                    for (DataSnapshot snapshot : dataSnapshot.child(currentMonth).getChildren()){
                        selectDate.add(snapshot.getKey()); // get current month purchase record date
                        for (DataSnapshot snapshot1: snapshot.child(AppConstant.PURCHASE_DATABASE_NODE_NAME).getChildren()){
                            getPurchaseRecord(snapshot1);
                        }
                    }
                    selectDateField.setItems(selectDate);
                }

                //set the column name and initialize column with database column
                String[] PurchaseColName = AppConstant.PURCHASE_TABLE_COLUMN_NAME;
                setTableData(purchaseProductTable,PurchaseColName,purchaseData);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {

            }
        });
    }


    private void getPurchaseRecord(DataSnapshot snapshot){

        for (DataSnapshot dataSnapshot1: snapshot.getChildren()){
            Purchase purchase = dataSnapshot1.getValue(Purchase.class);
            purchaseData.add(purchase);

            purchaseProductsDatabaseRef.add(dataSnapshot1.getRef()); //save all database reference for further uses
        }
    }


    //set the table column name and initialize the table column with the database column
    public void setTableData(TableView table, String[] colName, ObservableList tableData){

        table.getColumns().clear();

        String columnFor  = AppConstant.PURCHASE_DATABASE_NODE_NAME;
        //Initialize the table Column and column value
        for(String aColName : colName) {
            String name = aColName.replace(columnFor, "");
            TableColumn column;
            if(aColName.equals("Products")){
                column = new TableColumn("Action");
                column.setCellFactory(param -> new AddProductShowButton(table));
            } else {
                column = new TableColumn(name);
                column.setCellValueFactory(new PropertyValueFactory(aColName)); //indicate the table column to model column
            }
            table.getColumns().add(column);
        }
        table.setItems(tableData);            //Set All data to table
    }

    private void setComboBoxData(){

        DatabaseUtil.getDataForSingleValueEvent(AppConstant.INVOICES_DATABASE_NODE_NAME, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                if(selectMonth.size()>0) {
                    selectMonth.clear();
                }

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){ // Get all month from database and show them in combobox
                    selectMonth.add(snapshot.getKey());
                }
                selectMonthField.setItems(selectMonth);

                if(!selectMonth.isEmpty()) {
                    selectMonthField.getSelectionModel().select(currentMonth);
                }
            }

            @Override
            public void onFailure(DatabaseError databaseError) {

            }
        });

    }

    public void resetSelectedDate(ActionEvent actionEvent) {
        selectDateField.getSelectionModel().clearSelection();
        setTableColumnDataForPurchase(null);
    }

    public void addButtonAction(ActionEvent actionEvent) {

        JFXUtil.popUpWindows("layout/Purchase/PurchaseProduct.fxml",actionEvent);
    }

    public void editButtonAction(ActionEvent actionEvent) {
//        Supplier selectedSupplier =  (Supplier) purchaseProductTable.getSelectionModel().getSelectedItem();
//        EditSupplierController.editSupplier = selectedSupplier;
//        JFXUtil.popUpWindows("layout/Supplier/EditSupplier.fxml",actionEvent);
    }

    public void deleteButtonAction(ActionEvent actionEvent) {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Remove");
//        alert.setHeaderText("Remove Supplier");
//        alert.setContentText("Are you sure?");
//
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == ButtonType.OK) {
//            Supplier selectedSupplier =  (Supplier) supplyProductTable.getSelectionModel().getSelectedItem();
//            String supplierId = selectedSupplier.getSupplierId();
//            DatabaseUtil.delSupplier(supplierId);
//        }
//        supplyProductTable.getSelectionModel().clearSelection();
    }

    private class AddProductShowButton extends TableCell<Purchase, Boolean> {

        final JFXButton moreDetails = new JFXButton("More Details");

        final StackPane paddedButton = new StackPane();

        private String theme2Url = ClassLoader.getSystemResource("css/admin.css").toExternalForm();

        AddProductShowButton(final TableView table) {
            paddedButton.setPadding(new Insets(3));
            paddedButton.getChildren().add(moreDetails);
            paddedButton.getStylesheets().add(theme2Url);
            moreDetails.getStyleClass().add("menu-buttons-selected");

            // More-Details button click listener
            moreDetails.setOnAction(event -> {
                table.getSelectionModel().select(getTableRow().getIndex());
                int i = table.getSelectionModel().getSelectedIndex();
                ViewPurchasedProductController.selectedPurchaseData =  (Purchase) table.getSelectionModel().getSelectedItem();
                ViewPurchasedProductController.purchaseProductDatabaseRef = purchaseProductsDatabaseRef.get(i);
                JFXUtil.popUpWindows("layout/Purchase/ViewPurchaseProduct.fxml", event);
            });
        }

        @Override protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(paddedButton);
            } else {
                setGraphic(null);
            }
        }
    }

}
