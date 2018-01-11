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
import com.nerdgeeks.shop.Util.OnGetDataListener;
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
    public TextField searchField;
    public TableView productTableView;
    public TableView listTableView;
    public TextField productField;
    public TextField priceField;
    public TextField quantityField;
    public Label quantityLabel;
    public TextField subTotalField;
    public TextField vatField;
    public TextField discountField;
    public TextField netPayableField;
    public VBox sellPane;
    public JFXButton addButton;
    public JFXButton removeButton;
    public JFXButton paymentButton;
    private ObservableList<Product> stockData = FXCollections.observableArrayList();
    private ObservableList<Stock> stockCount = FXCollections.observableArrayList();
    private ObservableList<Product> filterList = FXCollections.observableArrayList();
    private ObservableList<SellProductModel> productModels = FXCollections.observableArrayList();
    private double temp;
    private String vat;
    private double tempVat;
    private FilteredList<Product> filteredData = new FilteredList<>(stockData, s -> true);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTableColumnDataForSupplier();
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
    }

    private void onButtonSetUpDisable(boolean isDisable) {
        quantityField.setDisable(isDisable);
        priceField.setDisable(isDisable);
        productField.setDisable(isDisable);
        if (quantityField.getText().isEmpty()){
            addButton.setDisable(true);
        }

        quantityField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    addButton.setDisable(true);
                } else {
                    addButton.setDisable(false);
                }
            }
        });
    }

    private void onFilterSellProductTableList() {
        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if(newValue == null || newValue.length() == 0) {
                    filteredData.setPredicate(s -> true);
                }
                else {
                    filteredData.setPredicate(s -> s.getProductName().toLowerCase().contains(newValue.toLowerCase()));
                }

//                if (newValue.equals("")) {
//                    String[] SellColName = {"productName"};
//                    JFXUtil.setTableData(productTableView, SellColName, stockData);
//                    quantityLabel.setText("Stock: ");
//                    filterList.clear();
//                    vat = "";
//                } else {
//                    if (filterList.size() > 0) {
//                        filterList.clear();
//                    }
//                    String value = newValue.toLowerCase();
//                    for (Product product : stockData) {
//                        String name = product.getProductName().toLowerCase();
//                        if (name.contains(value)) {
//                            filterList.add(product);
//                        }
//                    }
//                    //set the column name and initialize column with database column
//                    String[] SellColName = {"productName"};
//                    JFXUtil.setTableData(productTableView, SellColName, filteredData);
//                }
                //set the column name and initialize column with database column
                String[] SellColName = {"productName"};
                JFXUtil.setTableData(productTableView, SellColName, filteredData);
            }
        });
    }

    private void setTableColumnDataForSupplier() {
        //Firebase Database get data and set data to table
        DatabaseUtil.getDataValueEvent(AppConstant.STOCK_DATABASE_NODE_NAME, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (stockData.size() > 0) {
                    stockData.clear();
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Stock stock = snapshot.getValue(Stock.class);
                    String count = stock.getInStock();
                    Stock s = new Stock(snapshot.getKey(), count);
                    stockCount.add(s);
                    //Now get product details using product id from database
                    DatabaseUtil.firebaseDatabase.child(AppConstant.PRODUCTS_DATABASE_NODE_NAME).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot rootSupplier : dataSnapshot.getChildren()) {
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

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                //set the column name and initialize column with database column
                String[] SupplierColName = {"productName"};

                JFXUtil.setTableData(productTableView, SupplierColName, stockData);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {

            }
        });
    }

    public void logoutAction(ActionEvent actionEvent) {
    }

    public void addAction(ActionEvent actionEvent) {
        String product = productField.getText();
        String qt = quantityField.getText();
        if (qt.isEmpty()){
            return;
        }
        double unitPrice = Double.parseDouble(priceField.getText());
        int quantity = Integer.parseInt(qt);

        double total = quantity * unitPrice;
        SellProductModel model = new SellProductModel(product, unitPrice, quantity, total);
        productModels.add(model);

        temp += total;
        subTotalField.setText("" + temp);

        double v = (total * Double.parseDouble(vat)) / 100;
        tempVat += v;
        vatField.setText("" + tempVat);
        paymentButton.setDisable(false);
        resetAction(actionEvent);
    }

    public void removeAction(ActionEvent actionEvent) {
        int index = listTableView.getSelectionModel().getSelectedIndex();
        temp -= productModels.get(index).getTotal();
        productModels.remove(index);
        subTotalField.setText("" + temp);

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
    }

    public void clearAction(ActionEvent actionEvent) {
        listTableView.getItems().clear();
        temp = 0;
        tempVat = 0;
        subTotalField.setText("");
        vatField.setText("");
        paymentButton.setDisable(true);
    }
}
