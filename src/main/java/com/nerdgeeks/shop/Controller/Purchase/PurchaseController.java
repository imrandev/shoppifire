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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.net.URL;
import java.util.Optional;
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

    private ObservableList<String> selectMonthComboBoxData = FXCollections.observableArrayList();
    private ObservableList<String> selectDateComboBoxData = FXCollections.observableArrayList();

    private String currentMonth = JFXUtil.getCurrentMonth();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        resetButton.setDisable(true);
        deleteButton.disableProperty().bind(Bindings.isEmpty(purchaseProductTable.getSelectionModel().getSelectedItems()));
        purchaseProductPane.setOnMouseClicked(event -> purchaseProductTable.getSelectionModel().clearSelection());

        setComboBoxData();

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

//        purchaseProductTable.setRowFactory(row -> new TableRow<>() {
//            @Override
//            public void updateItem(Purchase item, boolean empty) {
//                super.updateItem(item, empty);
//
//                if (item == null || empty) {
//                    setStyle("");
//                } else {
//                    //Now 'item' has all the info of the Person in this row
//                    if (item.getPurchaseConfirm().equals("false")) {
//                        //We apply now the changes in all the cells of the row
//                        for (int i = 0; i < getChildren().size(); i++) {
//                            ((Labeled) getChildren().get(i)).setTextFill(Color.WHITE);
//                            getChildren().get(i).setStyle("-fx-background-color: red");
//                        }
//                    } else {
////                        if (getTableView().getSelectionModel().getSelectedItems().contains(item)) {
////                            for (int i = 0; i < getChildren().size(); i++) {
////                                ((Labeled) getChildren().get(i)).setTextFill(Color.WHITE);
////                            }
////                        } else {
////                            for (int i = 0; i < getChildren().size(); i++) {
////                                ((Labeled) getChildren().get(i)).setTextFill(Color.BLACK);
////                            }
////                        }
//                    }
//                }
//            }
//        });
    }


    private void setComboBoxData(){

        DatabaseUtil.getDataForSingleValueEvent(AppConstant.INVOICES_DATABASE_NODE_NAME, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(selectMonthComboBoxData.size()>0) {
                    selectMonthComboBoxData.clear();
                }

                // Get all month from database and show them in combobox
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    selectMonthComboBoxData.add(snapshot.getKey());
                }

                selectMonthField.setItems(selectMonthComboBoxData);
                selectMonthField.getSelectionModel().select(currentMonth);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {

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

                if (date != null){
                    for (DataSnapshot snapshot : dataSnapshot.child(currentMonth).child(date).child(AppConstant.PURCHASE_DATABASE_NODE_NAME).getChildren()){
                        getPurchaseRecord(snapshot);
                    }
                } else {
                    selectDateComboBoxData.clear();
                    for (DataSnapshot snapshot : dataSnapshot.child(currentMonth).getChildren()){
                        selectDateComboBoxData.add(snapshot.getKey()); // get current month purchase record date
                        for (DataSnapshot snapshot1: snapshot.child(AppConstant.PURCHASE_DATABASE_NODE_NAME).getChildren()){
                            getPurchaseRecord(snapshot1);
                        }
                    }
                    selectDateField.setItems(selectDateComboBoxData);
                }
            }

            @Override
            public void onFailure(DatabaseError databaseError) {

            }
        });

        //set the column name and initialize column with database column
        String[] PurchaseColName = AppConstant.PURCHASE_TABLE_COLUMN_NAME;
        setTableData(purchaseProductTable,PurchaseColName,purchaseData);
    }


    private void getPurchaseRecord(DataSnapshot snapshot){

        for (DataSnapshot dataSnapshot1: snapshot.getChildren()){

            Purchase purchase = dataSnapshot1.getValue(Purchase.class);
            purchaseData.add(purchase);

            //now save single purchase record  database reference for viewPurchaseProductController
            purchaseProductsDatabaseRef.add(dataSnapshot1.getRef());
        }
    }


    //set the table column name and initialize the table column with the database column
    private void setTableData(TableView table, String[] colName, ObservableList tableData){

        table.getColumns().clear();

        String columnFor  = AppConstant.PURCHASE_DATABASE_NODE_NAME;

        //Initialize the table Column and column value
        for(String aColName : colName) {
            String name = aColName.replace(columnFor, "");
            TableColumn column1;
            if(aColName.equals("Products") && tableData != null){
                column1 = new TableColumn("Action");
                column1.setCellFactory(param -> new AddProductShowButton(table)); // add button in action cell for every row
            } else {
                column1 = new TableColumn(name);
                column1.setCellValueFactory(new PropertyValueFactory(aColName)); //indicate the table column to model column
            }

            table.getColumns().add(column1);
        }
        table.setItems(tableData);            //Set All data to table
    }


    @FXML
    public void resetSelectedDate(ActionEvent actionEvent) {

        setTableColumnDataForPurchase(null);
        selectDateField.getSelectionModel().clearSelection();
    }

    @FXML
    public void addButtonAction(ActionEvent actionEvent) {

        JFXUtil.popUpWindows("layout/Purchase/PurchaseProduct.fxml");
    }

    @FXML
    public void deleteButtonAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove");
        alert.setHeaderText("Remove Purchase Record");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Purchase selectedPurchaseRecord =  purchaseProductTable.getSelectionModel().getSelectedItem();
            String purchaseMonth = selectedPurchaseRecord.getPurchaseMonth();
            String purchaseDate = selectedPurchaseRecord.getPurchaseDate();
            String purchaseSupplier = selectedPurchaseRecord.getSupplierName();
            String purchaseId = selectedPurchaseRecord.getPurchaseId();
            DatabaseUtil.delPurchaseRecord(purchaseMonth,purchaseDate,purchaseSupplier,purchaseId);
        }
        purchaseProductTable.getSelectionModel().clearSelection();
        setTableColumnDataForPurchase(null);
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
                JFXUtil.popUpWindows("layout/Purchase/ViewPurchaseProduct.fxml");
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
