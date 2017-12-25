package com.nerdgeeks.shop.Controller.Purchase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.nerdgeeks.shop.Model.Purchase;
import com.nerdgeeks.shop.Model.PurchaseProduct;
import com.nerdgeeks.shop.Util.AppConstant;
import com.nerdgeeks.shop.Util.DatabaseUtil;
import com.nerdgeeks.shop.Util.JFXUtil;
import com.nerdgeeks.shop.Util.OnGetDataListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
    public JFXButton addButton, editButton, deleteButton;
    public VBox purchaseProductPane;
    public JFXComboBox selectMonthField;
    public JFXComboBox selectDateField;
    public JFXButton resetButton;

    private ObservableList<Purchase> purchaseData = FXCollections.observableArrayList();
    private ObservableList<ObservableList<PurchaseProduct>> purchaseProductsData = FXCollections.observableArrayList();

    private ObservableList<String> selectMonth = FXCollections.observableArrayList();
    private ObservableList<String> selectDate = FXCollections.observableArrayList();

    private String currentMonth = JFXUtil.getCurrentMonth();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setTableColumnDataForPurchase(null, null);
        setComboBoxData();

        resetButton.setDisable(true);

        editButton.disableProperty().bind(Bindings.isEmpty(purchaseProductTable.getSelectionModel().getSelectedItems()));
        deleteButton.disableProperty().bind(Bindings.isEmpty(purchaseProductTable.getSelectionModel().getSelectedItems()));

        purchaseProductPane.setOnMouseClicked(event -> purchaseProductTable.getSelectionModel().clearSelection());

        selectMonthField.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                setTableColumnDataForPurchase(newValue.toString(),null);
            }
        });

        selectDateField.valueProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue != null){
                setTableColumnDataForPurchase(null, newValue.toString());
                resetButton.setDisable(false);
            } else {
                resetButton.setDisable(true);
            }
        });

        selectDateField.setItems(selectDate);
    }

    private void setTableColumnDataForPurchase(String month, String date){

      if (month != null){
          currentMonth = month;
      }

        //Firebase Databse get data and set data to table
        DatabaseUtil.getDataValueEvent(AppConstant.INVOICES_DATABASE_NODE_NAME, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                if(purchaseData.size()>0){
                    purchaseData.clear();
                }

                if (date != null){  // Get all purchase record value from database only selected date
                    for (DataSnapshot snapshot : dataSnapshot.child(currentMonth).child(date).child(AppConstant.PURCHASE_DATABASE_NODE_NAME).getChildren()){
                        for (DataSnapshot dataSnapshot1: snapshot.getChildren()){
                            Purchase purchase = dataSnapshot1.getValue(Purchase.class);
                            purchaseData.add(purchase);
                        }
                    }
                } else {  // Get all purchase record value from database

                    selectDate.clear();

                    for (DataSnapshot snapshot : dataSnapshot.child(currentMonth).getChildren()){

                        selectDate.add(snapshot.getKey()); // get current month purchase record date

                        for (DataSnapshot snapshot1: snapshot.child(AppConstant.PURCHASE_DATABASE_NODE_NAME).getChildren()){
                            for (DataSnapshot dataSnapshot1: snapshot1.getChildren()){
                                Purchase purchase = dataSnapshot1.getValue(Purchase.class);
                                purchaseData.add(purchase);
                                ObservableList<PurchaseProduct> oneSupplier = FXCollections.observableArrayList();
                                for (DataSnapshot rootProduct: dataSnapshot1.child("Products").getChildren()){
                                    PurchaseProduct product = rootProduct.getValue(PurchaseProduct.class);
                                    oneSupplier.add(product);
                                }
                                purchaseProductsData.add(oneSupplier);
                            }
                        }
                    }
                }

                //set the column name and initialize column with database column
                String[] PurchaseColName = AppConstant.PURCHASE_TABLE_COLUMN_NAME;
                setTableData(purchaseProductTable,PurchaseColName,"purchase", purchaseData);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {

            }
        });
    }

    //set the table column name and initialize the table column with the database column
    public void setTableData(TableView table, String[] colName, String columnFor, ObservableList tableData){

        table.getColumns().clear();

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
        DatabaseUtil.getDataValueEvent(AppConstant.INVOICES_DATABASE_NODE_NAME, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                if(selectMonth.size()>0) {
                    selectMonth.clear();
                }
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){ // Get all month from database and show them in combobox
                    selectMonth.add(snapshot.getKey());
                }
                selectMonthField.setItems(selectMonth);
                selectMonthField.getSelectionModel().select(currentMonth);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {

            }
        });
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

    public void resetSelectedDate(ActionEvent actionEvent) {
        selectDateField.getSelectionModel().clearSelection();
        setTableColumnDataForPurchase(null,null);
    }


    private class AddProductShowButton extends TableCell<Purchase, Boolean> {

        final JFXButton addButton = new JFXButton("More Details");

        final StackPane paddedButton = new StackPane();

        private String theme2Url = ClassLoader.getSystemResource("css/admin.css").toExternalForm();

        AddProductShowButton(final TableView table) {
            paddedButton.setPadding(new Insets(3));
            paddedButton.getChildren().add(addButton);
            paddedButton.getStylesheets().add(theme2Url);
            addButton.getStyleClass().add("menu-buttons-selected");
            addButton.setOnAction(event -> {
                table.getSelectionModel().select(getTableRow().getIndex());
                int i = table.getSelectionModel().getSelectedIndex();
                ViewPurchasedProductController.purchaseProductsData = purchaseProductsData.get(i);
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
