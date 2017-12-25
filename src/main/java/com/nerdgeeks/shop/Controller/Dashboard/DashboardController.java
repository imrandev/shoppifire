package com.nerdgeeks.shop.Controller.Dashboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;

import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;

public class DashboardController implements Initializable{

    @FXML
    public LineChart<String, Number> invoiceChart;
    @FXML
    public BarChart<String, Double> productsChart;
    @FXML
    public PieChart stockChart;

    @FXML
    CategoryAxis pxAxis;
    @FXML
    CategoryAxis ixAxis;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void loadInvoiceChart() {

        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        ObservableList lists = FXCollections.observableArrayList(months);
        XYChart.Series series = new XYChart.Series();

//        for (Invoice i : invoiceModel.getInvoices()) {
//            String month = convertDate(i.getDate());
//            series.getData().add(new XYChart.Data(month, i.getPayable()));
//        }

        series.setName("Sales");
        ixAxis.setCategories(lists);
        invoiceChart.getData().add(series);
    }

    private void loadProductsChart() {

        ObservableList lists = FXCollections.observableArrayList();
        XYChart.Series<String, Double> series = new XYChart.Series<>();

//        for (Product p : productModel.getProducts()) {
//            series.getData().add(new XYChart.Data(p.getProductName(), p.getQuantity()));
//            lists.add(p.getProductName());
//        }

        series.setName("Products");
        pxAxis.setCategories(lists);
        productsChart.getData().add(series);
    }

    private void loadStockChart(){

        ObservableList<PieChart.Data> lists = FXCollections.observableArrayList();

//        for(Product p : productModel.getProducts()){
//
//            lists.add(new PieChart.Data(p.getProductName(), p.getQuantity()));
//        }

        stockChart.getData().addAll(lists);
    }

    private String convertDate(String date) {

        int d = Integer.parseInt(date.substring(5, 7));
        return new DateFormatSymbols().getMonths()[d - 1];
    }


}
