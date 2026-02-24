package com.example.team31project2;

import javafx.beans.property.*;
import java.time.LocalDateTime;

class InventoryItem {
private final IntegerProperty itemId = new SimpleIntegerProperty();
    private final StringProperty itemName = new SimpleStringProperty();
    private final DoubleProperty quantity = new SimpleDoubleProperty();
    private final StringProperty unitType = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> expirationDate = new SimpleObjectProperty<>();

    public InventoryItem(int itemId, String itemName, double quantity, String unitType, LocalDateTime expirationDate) {
        this.itemId.set(itemId);
        this.itemName.set(itemName);
        this.quantity.set(quantity);
        this.unitType.set(unitType);
        this.expirationDate.set(expirationDate);
    }

    public int getItemId() { return itemId.get(); }
    public IntegerProperty itemIdProperty() { return itemId; }

    public String getItemName() { return itemName.get(); }
    public StringProperty itemNameProperty() { return itemName; }

    public double getQuantity() { return quantity.get(); }
    public DoubleProperty quantityProperty() { return quantity; }

    public String getUnitType() { return unitType.get(); }
    public StringProperty unitTypeProperty() { return unitType; }

    public LocalDateTime getExpirationDate() { return expirationDate.get(); }
    public ObjectProperty<LocalDateTime> expirationDateProperty() { return expirationDate; }
}


