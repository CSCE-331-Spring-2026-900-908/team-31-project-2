package com.example.team31project2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.Connection;

public class InventoryController {

    
   @FXML private TableView<InventoryItem> table;

    @FXML private TableColumn<InventoryItem, Number> colId;
    @FXML private TableColumn<InventoryItem, String> colName;
    @FXML private TableColumn<InventoryItem, Number> colQty;
    @FXML private TableColumn<InventoryItem, String> colUnit;
    @FXML private TableColumn<InventoryItem, java.time.LocalDateTime> colExp;

    @FXML 
    public void initialize(){
        colId.setCellValueFactory(data -> data.getValue().itemIdProperty());
        colName.setCellValueFactory(data -> data.getValue().itemNameProperty());
        colQty.setCellValueFactory(data -> data.getValue().quantityProperty());
        colUnit.setCellValueFactory(data -> data.getValue().unitTypeProperty());
        colExp.setCellValueFactory(data -> data.getValue().expirationDateProperty());

        table.setItems(inventoryList);
        loadInventoryFromDB();

        
        //table.getItems().setAll(new InventoryItem("Pair", 30, 20, 20,"2027-02-23 23:00:39.993329") );
    }
    private final ObservableList<InventoryItem> inventoryList = FXCollections.observableArrayList();

    private void loadInventoryFromDB(){
        try(Connection conn = DatabaseConnection.getConnection()){
            InventoryDAO dao  = new InventoryDAO(conn);
            inventoryList.setAll(dao.getAllItems());
            table.setItems(inventoryList);

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    
}


