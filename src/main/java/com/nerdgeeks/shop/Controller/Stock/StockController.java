package com.nerdgeeks.shop.Controller.Stock;

import com.nerdgeeks.shop.Model.Stock;
import com.nerdgeeks.shop.Util.AppConstant;
import com.nerdgeeks.shop.Util.DatabaseUtil;
import com.nerdgeeks.shop.Util.JFXUtil;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;


public class StockController implements Initializable {

    public VBox stockPane;
    public TableView stockTable;
    public ObservableList<Stock> stockData = DatabaseUtil.getStockDataWithProductDetails();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.print(stockData.size());

        //set the column name and initialize column with database column
        String[] stockTableColName = AppConstant.STOCK_TABLE_COLUMN_NAME;
        JFXUtil.setTableData(stockTable,stockTableColName, stockData);

        stockPane.setOnMouseClicked(event -> stockTable.getSelectionModel().clearSelection());

    }


}
