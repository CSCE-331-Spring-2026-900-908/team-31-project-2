package com.example.team31project2;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO{
    private final Connection conn; 

    public InventoryDAO (Connection Conn){
        this.conn = Conn;
    }

    

    public List<InventoryItem> getAllItems(){

        String sql = " SELECT item_id, item_name, quantity, unit_type, expiration_date \n FROM inventory\n ORDER BY item_name ";
        List<InventoryItem> items = new ArrayList<>();

        try(PreparedStatement ps = conn.prepareStatement(sql); 
            ResultSet rs = ps.executeQuery()) {
            while(rs.next()){
                int id = rs.getInt("item_id");
                String name = rs.getString("item_name");
                double qty = rs.getDouble("quantity");
                String unit = rs.getString("unit_type");

                Timestamp ts = rs.getTimestamp("expiration_date");
                LocalDateTime exp = (ts == null) ? null : ts.toLocalDateTime();

                items.add(new InventoryItem(id, name, qty, unit, exp));
            }
            
        }
        catch (SQLException e) {
        e.printStackTrace();
        }
      return items;
    }
}