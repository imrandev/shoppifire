package com.nerdgeeks.shop.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Purchase {

    private String purchaseId, supplierName, totalBuyingPrice, totalPaid, totalDue, purchaseMonth, purchaseDate, purchaseConfirm;

    ObservableList<PurchaseProduct> purchaseProducts = FXCollections.observableArrayList();

    public Purchase() {

    }

    public Purchase(String purchaseId, String supplierName, String totalBuyingPrice, String totalPaid, String totalDue, String purchaseMonth, String purchaseDate, String purchaseConfirm) {
        this.purchaseId = purchaseId;
        this.supplierName = supplierName;
        this.totalBuyingPrice = totalBuyingPrice;
        this.totalPaid = totalPaid;
        this.totalDue = totalDue;
        this.purchaseMonth = purchaseMonth;
        this.purchaseDate = purchaseDate;
        this.purchaseConfirm = purchaseConfirm;
    }

    public String getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(String totalDue) {
        this.totalDue = totalDue;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPurchaseConfirm() {
        return purchaseConfirm;
    }

    public void setPurchaseConfirm(String purchaseConfirm) {
        this.purchaseConfirm = purchaseConfirm;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getTotalBuyingPrice() {
        return totalBuyingPrice;
    }

    public void setTotalBuyingPrice(String totalBuyingPrice) {
        this.totalBuyingPrice = totalBuyingPrice;
    }

    public String getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(String totalPaid) {
        this.totalPaid = totalPaid;
    }

    public String getPurchaseMonth() {
        return purchaseMonth;
    }

    public void setPurchaseMonth(String purchaseMonth) {
        this.purchaseMonth = purchaseMonth;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public ObservableList<PurchaseProduct> getPurchaseProducts() {
        return purchaseProducts;
    }

    public void setPurchaseProducts(ObservableList<PurchaseProduct> purchaseProducts) {
        this.purchaseProducts = purchaseProducts;
    }
}
