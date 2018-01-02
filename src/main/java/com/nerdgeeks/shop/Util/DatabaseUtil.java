package com.nerdgeeks.shop.Util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.nerdgeeks.shop.Model.Product;
import com.nerdgeeks.shop.Model.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseUtil {

    public static DatabaseReference firebaseDatabase;

    //find the firebase database JSON config file
    private InputStream findFile() {

        String fileName = "shop-9c056-firebase-adminsdk-z5fb7-3c23768f33.json";

        // this is the path within the jar file
        InputStream input = getClass().getResourceAsStream("/resources/" + fileName);
        if (input == null) {
            // this is how we load file within editor (EX: Eclipse, Intellij IDE)
            input = getClass().getClassLoader().getResourceAsStream(fileName);
        }
        return input;
    }

    //connect the application to firebase database
    public boolean ConnectFirebase() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(findFile()))
                    .setDatabaseUrl("https://shop-9c056.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);

            //Initialize the firebase database
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatabaseUtil.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(DatabaseUtil.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    //get firebase database node value for realtime
    public static void getDataValueEvent(String databaseNodeName, OnGetDataListener dataListener) {

        firebaseDatabase.child(databaseNodeName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataListener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //get firebase database node value for single time
    public static void getDataForSingleValueEvent(String databaseNodeName, OnGetDataListener dataListener) {

        firebaseDatabase.child(databaseNodeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataListener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dataListener.onFailure(databaseError);
            }
        });
    }

    //Get all supplier name
    public static ObservableList getSupplierName() {

        ObservableList<String> supplierName = FXCollections.observableArrayList();

        firebaseDatabase.child(AppConstant.SUPPLIERS_DATABASE_NODE_NAME).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Supplier supplier = dataSnapshot1.getValue(Supplier.class);
                    supplierName.add(supplier.getSupplierName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return supplierName;
    }

    //remove supplier from database
    public static void delSupplier(String supplierId) {
        firebaseDatabase.child(AppConstant.SUPPLIERS_DATABASE_NODE_NAME)
                .child(supplierId)
                .removeValue((databaseError, databaseReference) -> {
                });
    }

    //remove products from database
    public static void delProduct(String supplierName, String productCategory, String productId) {
        firebaseDatabase.child(AppConstant.PRODUCTS_DATABASE_NODE_NAME)
                .child(supplierName)
                .child(productCategory)
                .child(productId)
                .removeValue((databaseError, databaseReference) -> {
                });
    }

    //get product Name by supplier
    public static ObservableList getProductNameForSupplier(String SupplierName){

        ObservableList productName = FXCollections.observableArrayList();

        firebaseDatabase.child(AppConstant.PRODUCTS_DATABASE_NODE_NAME).child(SupplierName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot rootCategory: dataSnapshot.getChildren()){
                    for(DataSnapshot rootProduct: rootCategory.getChildren()) {
                        Product product = rootProduct.getValue(Product.class);
                        productName.add(product.getProductName());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return productName;
    }


    //get product Id by supplier
    public static ObservableList getProductIdForSupplier(String SupplierName){

        ObservableList<String> productId = FXCollections.observableArrayList();

        firebaseDatabase.child(AppConstant.PRODUCTS_DATABASE_NODE_NAME).child(SupplierName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                for(DataSnapshot rootCategory: dataSnapshot1.getChildren()){
                    for(DataSnapshot rootProduct: rootCategory.getChildren()){
                        Product singleProduct = rootProduct.getValue(Product.class);
                        productId.add(singleProduct.getProductId());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return productId;
    }

    //get product Id by supplier
    public static ObservableList getProductBuyingPriceForSupplier(String SupplierName){

        ObservableList<String> productBuyingPrice = FXCollections.observableArrayList();

        firebaseDatabase.child(AppConstant.PRODUCTS_DATABASE_NODE_NAME).child(SupplierName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                for(DataSnapshot rootCategory: dataSnapshot1.getChildren()){
                    for(DataSnapshot rootProduct: rootCategory.getChildren()){
                        Product singleProduct = rootProduct.getValue(Product.class);
                        productBuyingPrice.add(singleProduct.getBuyingPrice());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return productBuyingPrice;
    }

}

