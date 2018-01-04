package com.nerdgeeks.shop.Controller.Stock;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nerdgeeks.shop.Model.Product;
import com.nerdgeeks.shop.Model.Stock;
import com.nerdgeeks.shop.Util.AppConstant;
import com.nerdgeeks.shop.Util.DatabaseUtil;
import com.nerdgeeks.shop.Util.JFXUtil;
import com.nerdgeeks.shop.Util.OnGetDataListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class StockController implements Initializable {


    public VBox stockPane;
    public TableView stockTable;
    private ObservableList<Stock> stockData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setTableColumnDataForStock();

        stockPane.setOnMouseClicked(event -> stockTable.getSelectionModel().clearSelection());

    }


    private void setTableColumnDataForStock(){

        DatabaseUtil.getDataForSingleValueEvent(AppConstant.STOCK_DATABASE_NODE_NAME, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(stockData.size()>0){
                    stockData.clear();
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Stock stock = snapshot.getValue(Stock.class);

                    //Now get product details using product id from database
                    DatabaseUtil.firebaseDatabase.child(AppConstant.PRODUCTS_DATABASE_NODE_NAME).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot rootSupplier: dataSnapshot.getChildren()){
                                for(DataSnapshot rootCategory: rootSupplier.getChildren()){
                                    for(DataSnapshot rootProduct: rootCategory.getChildren()){
                                        if (rootProduct.getKey().equals(snapshot.getKey())){
                                            Product product = rootProduct.getValue(Product.class);
                                            Stock stockWithProductDetails = new Stock(product.getProductId(),product.getProductName(),product.getProductCategory(),product.getProductSupplier(),stock.getInStock());
                                            stockData.add(stockWithProductDetails);
                                        }
                                    }
                                }
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

                //set the column name and initialize column with database column
                String[] stockTableColName = AppConstant.STOCK_TABLE_COLUMN_NAME;
                JFXUtil.setTableData(stockTable,stockTableColName, stockData);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {

            }
        });
    }
}
