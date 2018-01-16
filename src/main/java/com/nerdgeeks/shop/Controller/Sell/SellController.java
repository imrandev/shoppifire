package com.nerdgeeks.shop.Controller.Sell;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jfoenix.controls.JFXButton;
import com.nerdgeeks.shop.Model.Product;
import com.nerdgeeks.shop.Model.SellProductModel;
import com.nerdgeeks.shop.Model.Stock;
import com.nerdgeeks.shop.Util.AppConstant;
import com.nerdgeeks.shop.Util.DatabaseUtil;
import com.nerdgeeks.shop.Util.JFXUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class SellController implements Initializable {

    public TextField searchField, productField, priceField, quantityField,
                    subTotalField, vatField, discountField, netPayableField;

    public TableView productTableView, listTableView;
    public Label quantityLabel;
    public VBox sellPane;
    public JFXButton addButton, removeButton, paymentButton;

    private ObservableList<Product> stockData = FXCollections.observableArrayList();
    private ObservableList<Stock> stockCount = FXCollections.observableArrayList();
    private ObservableList<Product> filterList = FXCollections.observableArrayList();
    public static ObservableList<SellProductModel> productModels = FXCollections.observableArrayList();
    private FilteredList<Product> filteredData = new FilteredList<>(stockData, s -> true);

    private double total, subTotal, tempVat, netPayable;
    private String vat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setColumnDataForProductTable();
        onButtonSetUpDisable(true);

        productTableView.setRowFactory(tv -> {
            TableRow<ObservableList> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                Product product = (Product) productTableView.getSelectionModel().getSelectedItem();
                productField.setText(product.getProductName());
                priceField.setText(product.getSellingPrice());
                onButtonSetUpDisable(false);
                if (filterList.size() == 0) {
                    quantityLabel.setText("Stock: " + stockCount
                            .get(productTableView.getSelectionModel().getSelectedIndex()).getInStock());
                    vat = stockData.get(productTableView.getSelectionModel().getSelectedIndex()).getProductVat();
                } else {
                    for (Stock s : stockCount) {
                        if (s.getProductId().equals(
                                filterList.get(productTableView.getSelectionModel().getSelectedIndex()).getProductId())) {
                            quantityLabel.setText("Stock: " + s.getInStock());
                            vat = filterList.get(productTableView.getSelectionModel().getSelectedIndex()).getProductVat();
                        }
                    }
                }
            });
            return row;
        });

        onFilterSellProductTableList();

        String[] SellColName = {"productName", "unitPrice", "quantity", "total"};
        JFXUtil.setTableData(listTableView, SellColName, productModels);

        removeButton.disableProperty().bind(Bindings.isEmpty(listTableView.getSelectionModel().getSelectedItems()));
        sellPane.setOnMouseClicked(event -> {
            listTableView.getSelectionModel().clearSelection();
            //productTableView.getSelectionModel().clearSelection();
        });

        discountField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null || newValue.length() == 0) {
                    netPayableField.setText("" + netPayable);
                } else {
                    double total = netPayable - Double.parseDouble(newValue);
                    netPayableField.setText("" + total);
                    System.out.print(newValue);
                }
            }
        });
    }

    private void onButtonSetUpDisable(boolean isDisable) {
        quantityField.setDisable(isDisable);
        priceField.setDisable(isDisable);
        productField.setDisable(isDisable);
        if (quantityField.getText().isEmpty()){
            addButton.setDisable(true);
        }

        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()){
                addButton.setDisable(true);
            } else {
                addButton.setDisable(false);
            }
        });
    }

    private void onFilterSellProductTableList() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue == null || newValue.length() == 0) {
                filteredData.setPredicate(s -> true);
            }
            else {
                filteredData.setPredicate(s -> s.getProductName().toLowerCase().contains(newValue.toLowerCase()));
            }
            //set the column name and initialize column with database column
            String[] SellColName = {"productName"};
            JFXUtil.setTableData(productTableView, SellColName, filteredData);
        });
    }

    private void setColumnDataForProductTable() {
        //Firebase Database get data and set data to table
        DatabaseUtil.firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (stockData.size() > 0) {
                    stockData.clear();
                    stockCount.clear();
                }
                for (DataSnapshot snapshot : dataSnapshot.child(AppConstant.STOCK_DATABASE_NODE_NAME).getChildren()) {
                    Stock stock = snapshot.getValue(Stock.class);
                    String count = stock.getInStock();
                    Stock s = new Stock(snapshot.getKey(), count);
                    stockCount.add(s);
                    //Now get product details using product id from database
                    for (DataSnapshot rootSupplier : dataSnapshot.child(AppConstant.PRODUCTS_DATABASE_NODE_NAME).getChildren()) {
                        for (DataSnapshot rootCategory : rootSupplier.getChildren()) {
                            for (DataSnapshot rootProduct : rootCategory.getChildren()) {
                                if (rootProduct.getKey().equals(snapshot.getKey())) {
                                    Product product = rootProduct.getValue(Product.class);
                                    stockData.add(product);
                                }
                            }
                        }
                    }
                }

                clearAction();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //set the column name and initialize column with database column
        String[] ProductTableColName = {"productName"};
        JFXUtil.setTableData(productTableView, ProductTableColName, stockData);
    }

    public void logoutAction(ActionEvent actionEvent) {
    }

    public void addAction(ActionEvent actionEvent) {
        String product = productField.getText();
        Product product1 = (Product) productTableView.getSelectionModel().getSelectedItem();
        String id = product1.getProductId();
        String qt = quantityField.getText();
        if (qt.isEmpty()){
            return;
        }
        double unitPrice = Double.parseDouble(priceField.getText());
        int quantity = Integer.parseInt(qt);

        total = quantity * unitPrice;
        SellProductModel model = new SellProductModel(id, product, unitPrice, quantity, total);
        productModels.add(model);

        subTotal += total;
        subTotalField.setText("" + subTotal);

        double v = (total * Double.parseDouble(vat)) / 100;
        tempVat += v;
        vatField.setText("" + tempVat);
        paymentButton.setDisable(false);

        netPayable = subTotal + tempVat;
        netPayableField.setText("" + netPayable);

        resetAction(actionEvent);
    }


    public void removeAction(ActionEvent actionEvent) {
        int index = listTableView.getSelectionModel().getSelectedIndex();
        subTotal -= productModels.get(index).getTotal();
        subTotalField.setText("" + subTotal);

        productModels.remove(index);
        if (listTableView.getItems().isEmpty()) {
            paymentButton.setDisable(true);
        }
    }

    public void resetAction(ActionEvent actionEvent) {
        productField.setText("");
        priceField.setText("");
        productTableView.getSelectionModel().clearSelection();
        quantityLabel.setText("");
        quantityField.setText("");
        onButtonSetUpDisable(true);
    }

    public void paymentAction(ActionEvent actionEvent) {
        CheckoutController.totalAmount = netPayable;
        CheckoutController.sellProduct = productModels;
        JFXUtil.popUpWindows("layout/sell/Checkout.fxml");
    }

    public void clearAction() {
        listTableView.getItems().clear();
        subTotal = 0;
        tempVat = 0;
        netPayable = 0;
        netPayableField.setText("0.00");
        discountField.setText("0");
        subTotalField.setText("0.00");
        vatField.setText("0.00");
        paymentButton.setDisable(true);
    }
}
