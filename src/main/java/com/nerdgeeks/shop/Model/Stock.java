package com.nerdgeeks.shop.Model;

public class Stock {

    private String productId, productName, productCategory, productSupplier, inStock;

    public Stock() {
    }

    public Stock(String productId, String productName, String productCategory, String productSupplier, String inStock) {
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productSupplier = productSupplier;
        this.inStock = inStock;
    }

    public Stock(String inStock){
        this.inStock = inStock;
    }

    public Stock(String productId, String inStock){
        this.inStock = inStock;
        this.productId = productId;
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

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductSupplier() {
        return productSupplier;
    }

    public void setProductSupplier(String productSupplier) {
        this.productSupplier = productSupplier;
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String productInStock) {
        this.inStock = productInStock;
    }
}
