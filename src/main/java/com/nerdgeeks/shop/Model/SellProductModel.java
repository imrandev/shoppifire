package com.nerdgeeks.shop.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SellProductModel {
    private String productName;
    private double unitPrice;
    private int quantity;
    private double total;
    private ObservableList<Double> productModels = FXCollections.observableArrayList();
    private double t;

    public SellProductModel(String productName, double unitPrice, int quantity, double total) {
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.total = total;
        //productModels.add(total);
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total){
        this.total = total;
    }

    public void setSubTotalPrice(double temp){
        t += temp;
    }

    public double getSubTotalPrice(){
        return t;
    }
}
