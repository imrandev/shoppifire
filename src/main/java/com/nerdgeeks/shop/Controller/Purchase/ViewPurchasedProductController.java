package com.nerdgeeks.shop.Controller.Purchase;

import com.nerdgeeks.shop.Model.PurchaseProduct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewPurchasedProductController implements Initializable{

    public TableView viewPurchasedTable;
    public static ObservableList<PurchaseProduct> purchaseProductsData = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewPurchasedTable.getColumns().clear();

        String[] colName = {"productName","productQuantity"};


        //Initialize the table Column and column value
        for(String aColName : colName) {
            String name = aColName.replace("product", "");
            TableColumn column = new TableColumn(name);
            column.setCellValueFactory(new PropertyValueFactory(aColName)); //indicate the table column to model column
            viewPurchasedTable.getColumns().add(column);
        }

        viewPurchasedTable.setItems(purchaseProductsData);
    }

    @FXML
    public void closeAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
