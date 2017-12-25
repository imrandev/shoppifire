package com.nerdgeeks.shop.Model;

public class Product {

    private String productId, productName, buyingPrice, sellingPrice, productVat, productCategory, productSupplier;

    public Product() {
    }

    public Product(String productId, String productName, String buyingPrice, String sellingPrice, String productVat, String productCategory, String productSupplier) {
        this.productId = productId;
        this.productName = productName;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.productVat = productVat;
        this.productCategory = productCategory;
        this.productSupplier = productSupplier;
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

    public String getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(String buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getProductVat() {
        return productVat;
    }

    public void setProductVat(String productVat) {
        this.productVat = productVat;
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
}
