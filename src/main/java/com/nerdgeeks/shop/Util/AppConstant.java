package com.nerdgeeks.shop.Util;

public class AppConstant {

    public static final String PRODUCTS_DATABASE_NODE_NAME = "Products";

    public static final String SUPPLIERS_DATABASE_NODE_NAME = "Suppliers";

    public static final String INVOICES_DATABASE_NODE_NAME = "Invoices";

    public static final String PURCHASE_DATABASE_NODE_NAME = "Purchase";

    public static final String SELL_DATABASE_NODE_NAME = "Sell";

    public static final String STOCK_DATABASE_NODE_NAME = "Stock";

    //For Products Table View Column Name
    public static final String[] PRODUCTS_TABLE_COLUMN_NAME = {"productName", "buyingPrice",
            "sellingPrice", "productVat","productCategory","productSupplier"};

    //For Supplier Table View Column Name
    public static String[] SUPPLIERS_TABLE_COLUMN_NAME = {"supplierName","supplierNo", "supplierAddress","supplierDescription"};

    //For Supplier Table View Column Name
    public static String[] PURCHASE_TABLE_COLUMN_NAME = {"supplierName", "totalBuyingPrice","totalPaid","totalDue","purchaseMonth","purchaseDate","Products"};

    //For Stock Table View Column Name
    public static final String[] STOCK_TABLE_COLUMN_NAME = {"productId","productName","productCategory","productSupplier", "inStock"};
}
