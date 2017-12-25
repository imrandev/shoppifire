package com.nerdgeeks.shop.Model;

public class Supplier {

    private String supplierId, supplierName, supplierNo, supplierAddress, supplierDescription;

    public Supplier(){

    }

    public Supplier(String supplierId, String supplierName, String supplierNo, String supplierAddress, String supplierDescription) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierNo = supplierNo;
        this.supplierAddress = supplierAddress;
        this.supplierDescription = supplierDescription;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierDescription() {
        return supplierDescription;
    }

    public void setSupplierDescription(String supplierDescription) {
        this.supplierDescription = supplierDescription;
    }

}
