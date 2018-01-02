package com.nerdgeeks.shop.Model;

public class PurchaseProduct {

    private String productId, productName, productQuantity;

    public PurchaseProduct() {
    }

    public PurchaseProduct(String productId, String productName, String productQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.productQuantity = productQuantity;
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

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }
}
