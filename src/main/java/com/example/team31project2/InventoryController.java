package com.example.team31project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class InventoryController {

    
    @FXML
    private TableColumn<InventoryItem,String> Item; 

    @FXML
    private TableColumn<InventoryItem,Integer> Target; 

    @FXML
    private TableColumn<InventoryItem,Integer> Current; 

    @FXML 
    private TableView<InventoryItem> table; 

    @FXML 
    public void initialize(){
        Item.setCellValueFactory(data-> new javafx.beans.property.SimpleStringProperty(data.getValue().getItem()));
        Current.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getCurrent()));
        Target.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTarget())); 

        table.getItems().setAll(new InventoryItem("Pair", 30, 20), new InventoryItem("apple", 40,9));
    }
}


class InventoryItem {
    public String Item; 
    public Integer Target;
    public Integer Current; 

    public InventoryItem(String Item, Integer Target, Integer Current ){
        this.Current = Current; 
        this.Item = Item; 
        this.Target = Target;
    }

    public String getItem(){return Item;}
    public Integer getTarget(){return Target;}
    public Integer getCurrent(){return Current;}
}
