package com.nerdgeeks.shop.Model;

public class Product {

    private String productName, productCategory, productPrice, productQuantity, productVat;

    //For Table Column Name
    private static String[] colName = {"productName","productCategory", "productPrice","productQuantity", "productVat"};

    public Product(){

    }

    public Product(String productName, String productCategory, String productPrice, String productQuantity, String productVat) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productVat = productVat;
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

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductVat() {
        return productVat;
    }

    public void setProductVat(String productVat) {
        this.productVat = productVat;
    }

    public static String[] getColName(){
        return colName;
    }
}
