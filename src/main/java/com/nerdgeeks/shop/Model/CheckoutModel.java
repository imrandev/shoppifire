package com.nerdgeeks.shop.Model;

import javafx.collections.ObservableList;

public class CheckoutModel {
    private ObservableList<SellProductModel> Products;
    private double netTotal;

    public CheckoutModel() {
    }

    public CheckoutModel(ObservableList<SellProductModel> products, double netTotal) {
        Products = products;
        this.netTotal = netTotal;
    }

    public ObservableList<SellProductModel> getProducts() {
        return Products;
    }

    public void setProducts(ObservableList<SellProductModel> products) {
        Products = products;
    }

    public double getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(double netTotal) {
        this.netTotal = netTotal;
    }
}
