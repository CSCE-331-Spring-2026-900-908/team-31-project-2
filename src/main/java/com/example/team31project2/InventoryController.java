package com.example.team31project2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class InventoryController {

    //TABLE VALUES
    @FXML private TableView<InventoryItem> table;

    @FXML private TableColumn<InventoryItem, Number> colId;
    @FXML private TableColumn<InventoryItem, String> colName;
    @FXML private TableColumn<InventoryItem, Double> colQty;
    @FXML private TableColumn<InventoryItem, String> colUnit;
    @FXML private TableColumn<InventoryItem, java.time.LocalDateTime> colExp;
    @FXML private TableColumn<InventoryItem, Double> colTarg;

    //TEXT FIELDS
    @FXML private javafx.scene.control.TextField textId;
    @FXML private javafx.scene.control.TextField textName;
    @FXML private javafx.scene.control.TextField textQty;
    @FXML private javafx.scene.control.TextField textUnit;
    @FXML private javafx.scene.control.DatePicker textdate;
    @FXML private javafx.scene.control.TextField textTarg;

    public void initialize(){
        //LINK THE DB TO THE ROWS
        table.setEditable(true);

        colId.setCellValueFactory(data -> data.getValue().itemIdProperty());
        colName.setCellValueFactory(data -> data.getValue().itemNameProperty());
        colQty.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        colUnit.setCellValueFactory(data -> data.getValue().unitTypeProperty());
        colExp.setCellValueFactory(data -> data.getValue().expirationDateProperty());
        colTarg.setCellValueFactory(data -> data.getValue().targetProperty().asObject());

        colQty.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colTarg.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        
        //MAKE ROWS QTY AND TARGET EDITABLE 
        colQty.setOnEditCommit(event -> {
        InventoryItem item = event.getRowValue();
        Double newVal = event.getNewValue();
        Double oldVal = event.getOldValue();

        if (newVal == null || newVal < 0) { // simple validation
            table.refresh();
            return;
        }

        item.setQuantity(newVal);
        boolean ok = updateQuantityInDb(item.getItemId(), newVal);

        if (!ok) {          // revert on failure
            item.setQuantity(oldVal);
            table.refresh();
        }
        });

        colTarg.setOnEditCommit(event-> {
            InventoryItem item = event.getRowValue(); 
            Double newVal = event.getNewValue();
            Double oldVal = event.getOldValue(); 

            if(newVal == null || newVal < 0){
                table.refresh();
                return;
            }

            item.setTarget(newVal);
            boolean ok = updateTargetInDb(item.getItemId(), newVal);

            if (!ok) {          // revert on failure
            item.setTarget(oldVal);
            table.refresh();
        }
        });

        

        table.setItems(inventoryList);
        loadInventoryFromDB(); // LOADS ALL ITEMS FROM DB TO IT
        
    }

    //LOADING FUNCTION
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

    //UPADATE QUANITY FUNCTION
    private boolean updateQuantityInDb(int itemId, Double newCurrent) {
    String sql = "UPDATE inventory SET quantity = ? WHERE item_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setDouble(1, newCurrent);
        ps.setInt(2, itemId);

        return ps.executeUpdate() == 1; // true if one row updated
    } catch (Exception e) {
        e.printStackTrace();
        return false;
        }
    }

    private boolean updateTargetInDb (int itemId, Double newTarget){
        String sql = "UPDATE inventory SET target_val = ? WHERE item_id = ?";
        try(Connection conn = DatabaseConnection.getConnection(); 
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setDouble(1, newTarget);
            ps.setInt(2, itemId);
            return ps.executeUpdate() == 1; 
        }catch (Exception e) {
        e.printStackTrace();
        return false;
        }
    }

    @FXML
    private void addHandelr(){
        Integer id = Integer.parseInt(textId.getText().trim()); 
        String name = textName.getText().trim();
        Double quantity = Double.parseDouble(textQty.getText().trim());
        double target = Double.parseDouble(textTarg.getText().trim());
        String unit = textUnit.getText().trim();

        if (name.isEmpty() || unit.isEmpty()) return;
        if (target < 0 || quantity < 0) return;

        java.time.LocalDateTime exp = null;
        if (textdate != null && textdate.getValue() != null) {
            exp = textdate.getValue().atStartOfDay(); // midnight
        }

        boolean ok = insertItemInDb(id, name, quantity, unit, exp, target);
        if (!ok) return;


        InventoryItem newItem = new InventoryItem(id, name, target, unit, exp, target);
        inventoryList.add(newItem);

        textId.clear();
        textName.clear();
        textQty.clear();
        textTarg.clear();
        textUnit.clear();
        if (textdate != null) textdate.setValue(null);
    }

    private boolean insertItemInDb(int id, String name, double quantity, String unit, java.time.LocalDateTime exp, double target){
        String sql = "INSERT INTO inventory (item_id, item_name, quantity, unit_type, expiration_date, target_val) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){ 
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setDouble(3, quantity);
        ps.setString(4, unit);

        if (exp == null) ps.setTimestamp(5, null);
        else ps.setTimestamp(5, java.sql.Timestamp.valueOf(exp));

        ps.setDouble(6, target);

        return ps.executeUpdate() == 1;  
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        
                        
    }

    @FXML
    private void removeHandler(){
        try {
        int id = Integer.parseInt(textId.getText()); 
        
        if(id < 0 ){
            return;
        }

        boolean ok = removeFromDB(int id);
        if(!ok){return;}
        inventoryList.removeIf(item -> item.getItemId() == id);

        textId.clear();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    boolean removeFromDB

}


