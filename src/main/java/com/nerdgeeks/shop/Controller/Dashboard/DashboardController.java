package com.nerdgeeks.shop.Controller.Dashboard;

import com.nerdgeeks.shop.Model.Stock;
import com.nerdgeeks.shop.Util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable{

    public LineChart<String, Number> invoiceChart;
    public BarChart<String, Double> productsChart;
    public CategoryAxis pxAxis;

    private ObservableList<Stock> stock = DatabaseUtil.getStockDataWithProductDetails();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadProductsChart();
    }

    private void loadProductsChart() {

        ObservableList<String> lists = FXCollections.observableArrayList();
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        for (Stock p : stock) {
            series.getData().add(new XYChart.Data(p.getProductName(), Double.parseDouble(p.getInStock())));
            lists.add(p.getProductName());
        }

        series.setName("Product's in Stock");
        pxAxis.setCategories(lists);
        productsChart.getData().add(series);
    }


}
