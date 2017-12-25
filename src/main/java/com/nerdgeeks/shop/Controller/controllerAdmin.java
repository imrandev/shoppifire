//package com.nerdgeeks.shop.Controller;
//
//import com.nerdgeeks.shop.Model.Product;
//import com.nerdgeeks.shop.Model.Supplier;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.UserRecord;
//import com.google.firebase.database.*;
//import com.jfoenix.controls.JFXComboBox;
//import com.jfoenix.controls.JFXTextArea;
//import com.jfoenix.controls.JFXTextField;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.Initializable;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.VBox;
//
//import java.net.URL;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.ResourceBundle;
//import java.util.concurrent.ExecutionException;
//
//
//public class controllerAdmin implements Initializable{
//
//    public Accordion accordion;
//    public Button btnLogout;
//    public Label lblTime;
//
//
//    //Employee Pane
//    public TitledPane addEmployeePane;
//    public JFXTextField getEmpEmail;
//    public JFXTextField getEmpPass;
//    private String empEmail,empPassword;
//
//    //Product Pane
//    public TitledPane addProductPane;
//    public JFXComboBox getCategory;
//    public JFXTextField getName;
//    public JFXTextField getQuantity;
//    public JFXTextField getPrice;
//    public JFXTextField getVat;
//    public JFXComboBox getSupplier;
//    private String Category,Name,Quantity,Price,Vat;
//
//
//    private ObservableList<String> cat = FXCollections.observableArrayList();
//    private ObservableList<Product> productData = FXCollections.observableArrayList();
//
//
//    public Label lblUsrName;
//    public VBox dataPane;
//    public TableView adminPanelTable;
//
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//
//        //lblUsrName.setText(loggedInUserId);
//        String date = new SimpleDateFormat("dd").format(new Date());
//        lblTime.setText(date+"-"+getCurrentMonth());
//    }
//
//
//    //Logout Button OnClick
//    public void logout(ActionEvent actionEvent) {
//
//    }
//
//    //Add Product Button OnClick
//    public void addProduct(ActionEvent actionEvent) {
//
//        Category = getCategory.getSelectionModel().getSelectedItem().toString();
//        Name = getName.getText();
//        Quantity = getQuantity.getText();
//        Price = getPrice.getText();
//        Vat = getVat.getText();
//
//        if (!validateProductData())
//            return;
//
//        //Product product = new Product(Name,Category,Price,Quantity,Vat);
////        firebaseDatabase.child("Products").child(Category).child(Name).setValue(product, (databaseError, databaseReference) -> {
////            getName.setText("");
////            getPrice.setText("");
////            getQuantity.setText("");
////            getVat.setText("");
////            System.out.print("Product Add Successfully");
////        });
//    }
//
//    //set the table column name and initialize the table column with the database column
//    private void setTableData(String[] colName, String columnFor, ObservableList tableData){
//        adminPanelTable.getColumns().clear();
//        //Initialize the table Column and column value
//        for(String aColName : colName) {
//            String name = aColName.replace(columnFor, "");
//            TableColumn column = new TableColumn(name);
//            column.setCellValueFactory(new PropertyValueFactory(aColName)); //indicate the table column to model column
//            adminPanelTable.getColumns().add(column);
//            adminPanelTable.setItems(tableData);            //Set All data to table
//        }
//    }
//
//    public void addEmployee(ActionEvent actionEvent) throws ExecutionException, InterruptedException {
//         empEmail = getEmpEmail.getText();
//         empPassword = getEmpPass.getText();
//
//        if(!verification())
//            return;
//
//        String uid = empEmail.substring(0,empEmail.indexOf("@"));
//        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
//                .setUid(uid+"-emp")
//                .setEmail(empEmail)
//                .setDisplayName(empPassword)
//                .setPassword(empPassword);
//
//        UserRecord userRecord = FirebaseAuth.getInstance().createUserAsync(request).get();
//        System.out.println("Successfully Created one employee: " + userRecord.getUid());
//    }
//
//
//    private boolean validateProductData(){
//        if(Category.isEmpty()) {
//            getCategory.setPromptText("Required");
//            return false;
//        }else if (Name.isEmpty()) {
//            getName.setPromptText("Required");
//            return false;
//        }else if (Quantity.isEmpty()) {
//            getQuantity.setPromptText("Required");
//            return false;
//        }else if (Price.isEmpty()) {
//            getPrice.setPromptText("Required");
//            return false;
//        }else if (Vat.isEmpty()) {
//            Vat = "0";
//        }
//        return true;
//    }
//
////    private boolean validateSupplierData(){
////        if(supplier_Name.isEmpty()) {
////            supplierName.setPromptText("Required");
////            return false;
////        }else if (supplier_Contact.isEmpty()) {
////            supplierContact.setPromptText("Required");
////            return false;
////        }else if (supplier_Address.isEmpty()) {
////            supplierAddress.setPromptText("Required");
////            return false;
////        }else if (supplier_Des.isEmpty()) {
////            supplierDes.setPromptText("Required");
////            return false;
////        }
////        return true;
////    }
//
//    private boolean verification(){
//        if(empEmail.isEmpty()) {
//            getEmpEmail.setPromptText("Employee Email Required");
//            return false;
//        }else if (empPassword.isEmpty()) {
//            getEmpPass.setPromptText("Password Required");
//            return false;
//        }
//        return true;
//    }
//
//    private String getCurrentMonth(){
//        String[] month = {"January","February","March","April","May","June","July","August","September","October","November","December"};
//        String timeStamp = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
//        String currentMonth = timeStamp.substring(3,5);
//        return month[Integer.parseInt(currentMonth)-1]+"-"+timeStamp.substring(6);
//    }
//}
