package com.nerdgeeks.shop.Controller.Supplier;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.jfoenix.controls.JFXButton;
import com.nerdgeeks.shop.Model.Supplier;
import com.nerdgeeks.shop.Util.AppConstant;
import com.nerdgeeks.shop.Util.DatabaseUtil;
import com.nerdgeeks.shop.Util.OnGetDataListener;
import com.nerdgeeks.shop.Util.JFXUtil;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {

    public TableView supplierTable;
    public JFXButton addButton,editButton,deleteButton;
    public VBox supplierPane;
    private ObservableList<Supplier> supplierData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setTableColumnDataForSupplier();

        editButton.disableProperty().bind(Bindings.isEmpty(supplierTable.getSelectionModel().getSelectedItems()));
        deleteButton.disableProperty().bind(Bindings.isEmpty(supplierTable.getSelectionModel().getSelectedItems()));

        supplierPane.setOnMouseClicked(event -> supplierTable.getSelectionModel().clearSelection());

    }

    public void addButtonAction(ActionEvent actionEvent) {

        JFXUtil.popUpWindows("layout/Supplier/AddSupplier.fxml",actionEvent);
    }

    public void editButtonAction(ActionEvent actionEvent) {
        Supplier selectedSupplier =  (Supplier) supplierTable.getSelectionModel().getSelectedItem();
        EditSupplierController.editSupplier = selectedSupplier;
        JFXUtil.popUpWindows("layout/Supplier/EditSupplier.fxml",actionEvent);
    }

    public void deleteButtonAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove");
        alert.setHeaderText("Remove Supplier");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Supplier selectedSupplier =  (Supplier) supplierTable.getSelectionModel().getSelectedItem();
            String supplierId = selectedSupplier.getSupplierId();
            DatabaseUtil.delSupplier(supplierId);
        }
        supplierTable.getSelectionModel().clearSelection();
    }

    private void setTableColumnDataForSupplier(){

        //Firebase Databse get data and set data to table
        DatabaseUtil.getDataValueEvent(AppConstant.SUPPLIERS_DATABASE_NODE_NAME, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(supplierData.size()>0){
                    supplierData.clear();
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Supplier supplier = snapshot.getValue(Supplier.class);
                    supplierData.add(supplier);
                }
                //set the column name and initialize column with database column
                String[] SupplierColName = AppConstant.SUPPLIERS_TABLE_COLUMN_NAME;

                JFXUtil.setTableData(supplierTable,SupplierColName,"supplier", supplierData);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {

            }
        });
    }
}
