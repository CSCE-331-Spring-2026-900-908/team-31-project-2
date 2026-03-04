package com.example.team31project2;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for inventory items.
 *
 * @author Team-31
 */
public class InventoryDAO{
    private final Connection conn; 

    /**
     * Creates a DAO with an existing database connection.
     *
     * @param Conn database connection to use
     */
    public InventoryDAO (Connection Conn){
        this.conn = Conn;
    }

    /**
     * Retrieves all inventory items from the database.
     *
     * @return list of inventory items ordered by item id
     */
    public List<InventoryItem> getAllItems(){

        String sql = " SELECT item_id, item_name, quantity, expiration_date, target_val\n FROM inventory\n ORDER BY item_id ASC ";
            List<InventoryItem> items = new ArrayList<>();

        try(PreparedStatement ps = conn.prepareStatement(sql); 
            ResultSet rs = ps.executeQuery()) {
            while(rs.next()){
                int id = rs.getInt("item_id");
                String name = rs.getString("item_name");
                double qty = rs.getDouble("quantity");
                double targ = rs.getDouble("target_val");
                Timestamp ts = rs.getTimestamp("expiration_date");
                LocalDateTime exp = (ts == null) ? null : ts.toLocalDateTime();

                                items.add(new InventoryItem(id, name, qty, exp, targ));
            }
            
        }
        catch (SQLException e) {
        e.printStackTrace();
        }
      return items;
    }
}