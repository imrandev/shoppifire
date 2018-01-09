package com.nerdgeeks.shop.Controller.Product;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.jfoenix.controls.JFXButton;
import com.nerdgeeks.shop.Model.Product;
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

public class ProductController implements Initializable {

    public JFXButton addButton,editButton,deleteButton;
    public VBox productPane;
    public TableView productTable;

    private ObservableList<String> category = FXCollections.observableArrayList();
    private ObservableList<Product> productData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setTableColumnDataForProduct();

        editButton.disableProperty().bind(Bindings.isEmpty(productTable.getSelectionModel().getSelectedItems()));
        deleteButton.disableProperty().bind(Bindings.isEmpty(productTable.getSelectionModel().getSelectedItems()));

        productPane.setOnMouseClicked(event -> productTable.getSelectionModel().clearSelection());

    }

    public void addButtonAction(ActionEvent actionEvent) {
        //data passing to controller class for further use
        AddProductController.category = category;
        JFXUtil.popUpWindows("layout/Product/AddProduct.fxml");
    }

    public void editButtonAction(ActionEvent actionEvent) {

        Product selectedProduct =  (Product) productTable.getSelectionModel().getSelectedItem();
        UpdateProductController.updateProduct = selectedProduct;
        UpdateProductController.category = category;
        JFXUtil.popUpWindows("layout/Product/UpdateProduct.fxml");
    }


    public void deleteButtonAction(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove");
        alert.setHeaderText("Remove Product");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Product selectedProduct =  (Product) productTable.getSelectionModel().getSelectedItem();

            String supplierName = selectedProduct.getProductSupplier();
            String productId = selectedProduct.getProductId();
            String productCategory = selectedProduct.getProductCategory();

            DatabaseUtil.delProduct(supplierName,productCategory,productId);
        }
        productTable.getSelectionModel().clearSelection();
    }

        private void setTableColumnDataForProduct(){

        //Firebase Databse get data and set data to table
        DatabaseUtil.getDataValueEvent(AppConstant.PRODUCTS_DATABASE_NODE_NAME, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(productData.size()>0){
                    productData.clear();
                }
                if(category.size()>0){
                    category.clear();
                }
                for (DataSnapshot rootSupplier: dataSnapshot.getChildren()) {
                    for (DataSnapshot rootCategory: rootSupplier.getChildren()){

                        String catName = rootCategory.getKey();
                        if (!category.contains(catName)){
                            category.add(catName);
                        }

                        for (DataSnapshot rootProduct: rootCategory.getChildren()){
                            Product product = rootProduct.getValue(Product.class);
                            productData.add(product);
                        }
                    }
                }

                //set the column name and initialize column with database column
                String[] productsColName = AppConstant.PRODUCTS_TABLE_COLUMN_NAME;

                JFXUtil.setTableData(productTable,productsColName, productData);

            }
            @Override
            public void onFailure(DatabaseError databaseError) {

            }
        });
    }
}
