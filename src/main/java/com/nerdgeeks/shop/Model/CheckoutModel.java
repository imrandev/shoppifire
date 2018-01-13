package com.nerdgeeks.shop.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CheckoutModel {
    private ObservableList<SellProductModel> Products = FXCollections.observableArrayList();
    private double netTotal;

    public CheckoutModel(ObservableList<SellProductModel> Products, double netTotal) {
        this.Products = Products;
        this.netTotal = netTotal;
    }

    public ObservableList<SellProductModel> getProducts() {
        return Products;
    }

    public void setProducts(ObservableList<SellProductModel> Products) {
        this.Products = Products;
    }

    public double getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(double netTotal) {
        this.netTotal = netTotal;
    }
}
