package com.nerdgeeks.shop.Controller.Sell;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jfoenix.controls.JFXButton;
import com.nerdgeeks.shop.MainApp;
import com.nerdgeeks.shop.Model.Product;
import com.nerdgeeks.shop.Model.SellProductModel;
import com.nerdgeeks.shop.Model.Stock;
import com.nerdgeeks.shop.PDF.CreatePDF;
import com.nerdgeeks.shop.Util.AppConstant;
import com.nerdgeeks.shop.Util.DatabaseUtil;
import com.nerdgeeks.shop.Util.JFXUtil;
import com.nerdgeeks.shop.Util.OnGetDataListener;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SellController implements Initializable {

    public TextField searchField, productField, priceField, quantityField,
                    subTotalField, vatField, discountField, netPayableField;

    public TableView productTableView, listTableView;
    public Label quantityLabel;
    public JFXButton addButton, removeButton, paymentButton;
    public Button logoutButton;
    public VBox header;
    public VBox anchorPane;
    public TextField vatAmount;
    public Button maxButton;

    private ObservableList<Product> stockData = FXCollections.observableArrayList();
    private ObservableList<Stock> stockCount = FXCollections.observableArrayList();
    static ObservableList<SellProductModel> productModels = FXCollections.observableArrayList();
    private FilteredList<Product> filteredData = new FilteredList<>(stockData, s -> true);

    private double subTotal;
    private double netPayable;
    private double xOffset = 0;
    private double yOffset = 0;
    private double total;
    private double v;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setColumnDataForProductTable();
        onButtonSetUpDisable(true);

        productTableView.setRowFactory(tv -> {
            TableRow<ObservableList> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                try{
                    Product product = (Product) productTableView.getSelectionModel().getSelectedItem();
                    int index = productTableView.getSelectionModel().getSelectedIndex();

                    if (index >-1 && product != null){
                        productField.setText(product.getProductName());
                        priceField.setText(product.getSellingPrice());
                        onButtonSetUpDisable(false);

                        for (Stock s : stockCount) {
                            if (s.getProductId().compareTo(
                                    product.getProductId())==0) {
                                quantityLabel.setText("Stock: " + s.getInStock());
                            }
                        }
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            });
            return row;
        });

        onFilterSellProductTableList();

        String[] SellColName = {"productName", "unitPrice", "quantity", "total"};
        JFXUtil.setTableData(listTableView, SellColName, productModels);

        removeButton.disableProperty().bind(Bindings.isEmpty(listTableView.getSelectionModel().getSelectedItems()));
        anchorPane.setOnMouseClicked(event -> {
            listTableView.getSelectionModel().clearSelection();
        });

        discountField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null || newValue.length() == 0) {
                    netPayableField.setText("" + netPayable);
                } else {
                    total = netPayable - Double.parseDouble(newValue);
                    netPayableField.setText("" + total);
                }
            }
        });

        onMouseMaximizeWindow();

        subTotalField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                double value = Double.valueOf(newValue);
                if (!vatField.getText().isEmpty()){
                    v = ( value * Double.parseDouble(vatField.getText())) / 100;
                    netPayable = value + v;
                    vatAmount.setText("" + v);
                    netPayableField.setText("" + netPayable);
                } else {
                    netPayable = value;
                    netPayableField.setText("" + value);
                }
                System.out.print("" + netPayable);
            }
        });

        vatField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")){
                    v = (subTotal * Double.parseDouble(newValue)) / 100;
                    netPayable += v;
                    vatAmount.setText("" + v);
                } else {
                    netPayable = subTotal;
                    vatAmount.setText("0.0");
                }
                netPayableField.setText("" + netPayable);
            }
        });
    }

    private void onMouseMaximizeWindow() {
        header.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resetAction();
                if (event.getButton().equals(MouseButton.PRIMARY)){
                    if (event.getClickCount()==2){
                        onChangedMaxWindowIcon();
                    }
                }
            }
        });
    }

    private void onChangedMaxWindowIcon() {
        Stage currentStage = (Stage) anchorPane.getScene().getWindow();
        if (currentStage.isFullScreen()){
            String image = SellController.class.getResource("/images/maximize_window.png").toExternalForm();
            maxButton.setStyle("-fx-background-image: url('" + image + "'); " +
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: stretch;");
            currentStage.setFullScreen(false);
        } else {
            String image = SellController.class.getResource("/images/normal_window.png").toExternalForm();
            maxButton.setStyle("-fx-background-image: url('" + image + "'); " +
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: stretch;");
            currentStage.setFullScreenExitHint("");
            currentStage.setFullScreen(true);
        }
    }

    private void onButtonSetUpDisable(boolean isDisable) {
        quantityField.setDisable(isDisable);
        priceField.setDisable(isDisable);
        productField.setDisable(isDisable);
        if (quantityField.getText().isEmpty()){
            addButton.setDisable(true);
        }

        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()){
                addButton.setDisable(true);
            } else {
                addButton.setDisable(false);
            }
        });
    }

    private void onFilterSellProductTableList() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue == null || newValue.length() == 0) {
                filteredData.setPredicate(s -> true);
            }
            else {
                filteredData.setPredicate(s -> s.getProductName().toLowerCase().contains(newValue.toLowerCase()));
            }
            //set the column name and initialize column with database column
            String[] SellColName = {"productName"};
            JFXUtil.setTableData(productTableView, SellColName, filteredData);
        });
    }

    private void setColumnDataForProductTable() {
        //Firebase Database get data and set data to table
        DatabaseUtil.firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (stockData.size() > 0) {
                    stockData.clear();
                    stockCount.clear();
                }
                for (DataSnapshot snapshot : dataSnapshot.child(AppConstant.STOCK_DATABASE_NODE_NAME).getChildren()) {
                    Stock stock = snapshot.getValue(Stock.class);
                    String count = stock.getInStock();
                    Stock s = new Stock(snapshot.getKey(), count);
                    stockCount.add(s);
                    //Now get product details using product id from database
                    for (DataSnapshot rootSupplier : dataSnapshot.child(AppConstant.PRODUCTS_DATABASE_NODE_NAME).getChildren()) {
                        for (DataSnapshot rootCategory : rootSupplier.getChildren()) {
                            for (DataSnapshot rootProduct : rootCategory.getChildren()) {
                                if (rootProduct.getKey().equals(snapshot.getKey())) {
                                    Product product = rootProduct.getValue(Product.class);
                                    stockData.add(product);
                                }
                            }
                        }
                    }
                }
                clearTable(false);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //set the column name and initialize column with database column
        String[] ProductTableColName = {"productName"};
        JFXUtil.setTableData(productTableView, ProductTableColName, stockData);
    }

    public void logoutAction(ActionEvent actionEvent) throws IOException {
        Parent root;
        URL myFxmlURL = ClassLoader.getSystemResource("layout/MainApp.fxml");
        root = FXMLLoader.load(myFxmlURL);
        Scene newScene = new Scene(root);

        Stage currentStage = (Stage) anchorPane.getScene().getWindow();

        if (currentStage.isFullScreen()){
            currentStage.setScene(newScene);
            currentStage.setFullScreen(true);
        } else {
            currentStage.setScene(newScene);
        }

        currentStage.setResizable(true);
        currentStage.show();
        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent event) -> {
            currentStage.setX(event.getScreenX() - xOffset);
            currentStage.setY(event.getScreenY() - yOffset);
        });
    }

    public void addAction(ActionEvent actionEvent) {
        String product = productField.getText();

        Product product1 = (Product) productTableView.getSelectionModel().getSelectedItem();
        String id = product1.getProductId();
        String qt = quantityField.getText();
        if (qt.isEmpty()){
            return;
        }
        double unitPrice = Double.parseDouble(priceField.getText());
        int quantity = Integer.parseInt(qt);

        total = quantity * unitPrice;
        SellProductModel model = new SellProductModel(id, product, unitPrice, quantity, total);
        productModels.add(model);

        subTotal += total;
        subTotalField.setText("" + subTotal);

        paymentButton.setDisable(false);
        discountField.setDisable(false);
        resetAction();
    }

    public void removeAction(ActionEvent actionEvent) {
        int index = listTableView.getSelectionModel().getSelectedIndex();
        subTotal -= productModels.get(index).getTotal();
        netPayable = subTotal;
        productModels.remove(index);
        subTotalField.setText("" + subTotal);

        if (listTableView.getItems().isEmpty()) {
            paymentButton.setDisable(true);
            discountField.setDisable(true);
        }
    }

    public void resetAction() {
        productField.setText("");
        priceField.setText("");
        productTableView.getSelectionModel().clearSelection();
        quantityLabel.setText("");
        quantityField.setText("");
        onButtonSetUpDisable(true);
    }

    public void paymentAction(ActionEvent actionEvent) {
        System.out.print("" + netPayable);
        CheckoutController.totalAmount = netPayable;
        CheckoutController.Products = productModels;
        JFXUtil.popUpWindows("layout/sell/Checkout.fxml");
    }

    @FXML
    public void clearAction() {
        clearTable(true);
    }

    private void clearTable(boolean b) {
        if (b){
            listTableView.getItems().clear();
        }
        subTotal = 0;
        netPayable = 0;
        subTotalField.setText("0.0");
        vatField.setText("");
        netPayableField.setText("0.0");
        discountField.setText("");
        discountField.setDisable(true);
        paymentButton.setDisable(true);
    }

    public void minusAction(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public void closeAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void maxAction(ActionEvent actionEvent) {
        if (actionEvent.getEventType() == ActionEvent.ACTION){
            onChangedMaxWindowIcon();
        }
    }
}
