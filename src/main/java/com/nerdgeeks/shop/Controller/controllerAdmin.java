package com.nerdgeeks.shop.Controller;

import com.nerdgeeks.shop.Model.Product;
import com.nerdgeeks.shop.Model.Supplier;
import com.nerdgeeks.shop.Util.FireDatabase;
import com.nerdgeeks.shop.Util.OnGetDataListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.*;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import static com.nerdgeeks.shop.Controller.controllerLogin.loggedInUserId;


public class controllerAdmin implements Initializable{

    public Accordion accordion;
    public Button btnLogout;
    public Label lblTime;

    //Supplier Pane
    public TitledPane addSupplierPane;
    public JFXTextField supplierName;
    public JFXTextField supplierContact;
    public JFXTextArea supplierAddress;
    public JFXTextArea supplierDes;
    private String supplier_Name,supplier_Contact,supplier_Address,supplier_Des;

    //Employee Pane
    public TitledPane addEmployeePane;
    public JFXTextField getEmpEmail;
    public JFXTextField getEmpPass;
    private String empEmail,empPassword;

    //Product Pane
    public TitledPane addProductPane;
    public JFXComboBox getCategory;
    public JFXTextField getName;
    public JFXTextField getQuantity;
    public JFXTextField getPrice;
    public JFXTextField getVat;
    private String Category,Name,Quantity,Price,Vat;


    private ObservableList<String> cat = FXCollections.observableArrayList();
    private ObservableList<Product> productData = FXCollections.observableArrayList();
    private ObservableList<Supplier> supplierData = FXCollections.observableArrayList();

    private DatabaseReference firebaseDatabase;
    public Label lblUsrName;
    public VBox dataPane;
    public TableView adminPanelTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //lblUsrName.setText(loggedInUserId);
        String date = new SimpleDateFormat("dd").format(new Date());
        lblTime.setText(date+"-"+getCurrentMonth());

        //Initialize the firebase database
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        setTableColumnDataForSupplier();

        addSupplierPane.setOnMouseClicked(event -> setTableColumnDataForSupplier());
        addProductPane.setOnMouseClicked(event -> setTableColumnDataForProduct());

    }

    //Logout Button OnClick
    public void logout(ActionEvent actionEvent) {

    }

    //Adding Supplier
    public void addSupplier(ActionEvent actionEvent) {

        supplier_Name = supplierName.getText();
        supplier_Contact = supplierContact.getText();
        supplier_Address = supplierAddress.getText();
        supplier_Des = supplierDes.getText();

        if(!validateSupplierData())
            return;

        String supplierId = firebaseDatabase.push().getKey();

        Supplier supplier = new Supplier(supplierId,supplier_Name,supplier_Contact,supplier_Address,supplier_Des);
        firebaseDatabase.child("Suppliers").child(supplier_Name).setValue(supplier, (databaseError, databaseReference) -> {
            supplierName.setText("");
            supplierContact.setText("");
            supplierAddress.setText("");
            supplierDes.setText("");
            System.out.print("Supplier Add Successfully");
        });
    }


    //Add Product Button OnClick
    public void addProduct(ActionEvent actionEvent) {

        Category = getCategory.getSelectionModel().getSelectedItem().toString();
        Name = getName.getText();
        Quantity = getQuantity.getText();
        Price = getPrice.getText();
        Vat = getVat.getText();

        if (!validateProductData())
            return;

        Product product = new Product(Name,Category,Price,Quantity,Vat);
        firebaseDatabase.child("Products").child(Category).child(Name).setValue(product, (databaseError, databaseReference) -> {
            getName.setText("");
            getPrice.setText("");
            getQuantity.setText("");
            getVat.setText("");
            System.out.print("Product Add Successfully");
        });
    }

    //set the table column name and initialize the table column with the database column
    private void setTableData(String[] colName, String columnFor, ObservableList tableData){
        adminPanelTable.getColumns().clear();
        //Initialize the table Column and column value
        for(String aColName : colName) {
            String name = aColName.replace(columnFor, "");
            TableColumn column = new TableColumn(name);
            column.setCellValueFactory(new PropertyValueFactory(aColName)); //indicate the table column to model column
            adminPanelTable.getColumns().add(column);
            adminPanelTable.setItems(tableData);            //Set All data to table
        }
    }

    private void setTableColumnDataForSupplier(){

        //Firebase Databse get data and set data to table
        FireDatabase.getDataValueEvent(firebaseDatabase.child("Suppliers"), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(supplierData.size()>0){
                    supplierData.clear();
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Supplier supplier = snapshot.getValue(Supplier.class);
                    supplierData.add(supplier);
                }
                //set the column name and initialize column with database column
                String[] SupplierColName = Supplier.getSupplierColName();
                setTableData(SupplierColName,"supplier", supplierData);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {

            }
        });

    }

    private void setTableColumnDataForProduct(){

        //Firebase Databse get data and set data to table
        FireDatabase.getDataValueEvent(firebaseDatabase.child("Products"), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(productData.size()>0){
                    productData.clear();
                }
                if(cat.size()>0){
                    cat.clear();
                }
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String name = snapshot.getKey();
                    cat.add(name);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        Product product = snapshot1.getValue(Product.class);
                        productData.add(product);
                    }
                }

                //set the column name and initialize column with database column
                String[] colName = Product.getColName();
                setTableData(colName,"product", productData);
                getCategory.setItems(cat);
            }
            @Override
            public void onFailure(DatabaseError databaseError) {

            }
        });
    }

    public void addEmployee(ActionEvent actionEvent) throws ExecutionException, InterruptedException {
         empEmail = getEmpEmail.getText();
         empPassword = getEmpPass.getText();

        if(!verification())
            return;

        String uid = empEmail.substring(0,empEmail.indexOf("@"));
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setUid(uid+"-emp")
                .setEmail(empEmail)
                .setDisplayName(empPassword)
                .setPassword(empPassword);

        UserRecord userRecord = FirebaseAuth.getInstance().createUserAsync(request).get();
        System.out.println("Successfully Created one employee: " + userRecord.getUid());
    }


    private boolean validateProductData(){
        if(Category.isEmpty()) {
            getCategory.setPromptText("Required");
            return false;
        }else if (Name.isEmpty()) {
            getName.setPromptText("Required");
            return false;
        }else if (Quantity.isEmpty()) {
            getQuantity.setPromptText("Required");
            return false;
        }else if (Price.isEmpty()) {
            getPrice.setPromptText("Required");
            return false;
        }else if (Vat.isEmpty()) {
            Vat = "0";
        }
        return true;
    }

    private boolean validateSupplierData(){
        if(supplier_Name.isEmpty()) {
            supplierName.setPromptText("Required");
            return false;
        }else if (supplier_Contact.isEmpty()) {
            supplierContact.setPromptText("Required");
            return false;
        }else if (supplier_Address.isEmpty()) {
            supplierAddress.setPromptText("Required");
            return false;
        }else if (supplier_Des.isEmpty()) {
            supplierDes.setPromptText("Required");
            return false;
        }
        return true;
    }

    private boolean verification(){
        if(empEmail.isEmpty()) {
            getEmpEmail.setPromptText("Employee Email Required");
            return false;
        }else if (empPassword.isEmpty()) {
            getEmpPass.setPromptText("Password Required");
            return false;
        }
        return true;
    }

    private String getCurrentMonth(){
        String[] month = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        String currentMonth = timeStamp.substring(3,5);
        return month[Integer.parseInt(currentMonth)-1]+"-"+timeStamp.substring(6);
    }
}
