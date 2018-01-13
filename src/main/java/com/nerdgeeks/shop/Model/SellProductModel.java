package com.nerdgeeks.shop.Model;

public class SellProductModel {
    private String productName;
    private double unitPrice;
    private int quantity;
    private double total;
    private String id;

    public SellProductModel(String id, String productName, int quantity, double total) {
        this.id = id;
        this.productName = productName;
        this.total = total;
        this.quantity = quantity;
    }

    public SellProductModel(String id, String productName, double unitPrice, int quantity, double total) {
        this.id = id;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

}
