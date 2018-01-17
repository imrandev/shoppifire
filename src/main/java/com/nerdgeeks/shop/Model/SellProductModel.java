package com.nerdgeeks.shop.Model;

public class SellProductModel {
    private String productId, productName;
    private double unitPrice;
    private int quantity;
    private double total;

    public SellProductModel() {
    }

    public SellProductModel(String productId, String productName, double unitPrice, int quantity, double total) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.total = total;
    }

    public SellProductModel(String productId, String productName, int quantity, double total) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.total = total;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public void setTotal(double total) {
        this.total = total;
    }
}
