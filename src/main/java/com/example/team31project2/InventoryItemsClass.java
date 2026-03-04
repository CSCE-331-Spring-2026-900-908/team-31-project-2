package com.example.team31project2;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import java.time.LocalDateTime;

/**
 * Represents an inventory item and its tracked properties.
 *
 * @author Team-31
 */
class InventoryItem {
private final IntegerProperty itemId = new SimpleIntegerProperty();
    private final StringProperty itemName = new SimpleStringProperty();
    private final DoubleProperty quantity = new SimpleDoubleProperty();
    private final ObjectProperty<LocalDateTime> expirationDate = new SimpleObjectProperty<>();
    private final DoubleProperty target = new SimpleDoubleProperty();
    private final ReadOnlyDoubleWrapper fillRatio = new ReadOnlyDoubleWrapper();

    /**
     * Creates a new inventory item.
     *
     * @param itemId item identifier
     * @param itemName item name
     * @param quantity current quantity
     * @param expirationDate expiration date, or null if unknown
     * @param target target quantity
     */
    public InventoryItem(int itemId, String itemName, double quantity, LocalDateTime expirationDate, double target) {
        this.itemId.set(itemId);
        this.itemName.set(itemName);
        this.quantity.set(quantity);
        this.expirationDate.set(expirationDate);
        this.target.set(target);

        fillRatio.bind(Bindings.createDoubleBinding(() -> {
            double targetVal = this.target.get();
            if (targetVal <= 0) {
                return 0.0;
            }
            double ratio = this.quantity.get() / targetVal;
            return Math.max(0.0, Math.min(1.0, ratio));
        }, this.quantity, this.target));
    }

    /**
     * Gets the item id.
     *
     * @return item id
     */
    public int getItemId() { return itemId.get(); }

    /**
     * Gets the item id property.
     *
     * @return item id property
     */
    public IntegerProperty itemIdProperty() { return itemId; }

    /**
     * Gets the item name.
     *
     * @return item name
     */
    public String getItemName() { return itemName.get(); }

    /**
     * Gets the item name property.
     *
     * @return item name property
     */
    public StringProperty itemNameProperty() { return itemName; }

    /**
     * Gets the current quantity.
     *
     * @return current quantity
     */
    public double getQuantity() { return quantity.get(); }

    /**
     * Sets the current quantity.
     *
     * @param v new quantity
     */
    public void setQuantity(Double v) { quantity.set(v); }

    /**
     * Gets the quantity property.
     *
     * @return quantity property
     */
    public DoubleProperty quantityProperty() { return quantity; }

    /**
     * Gets the expiration date.
     *
     * @return expiration date, or null if not set
     */
    public LocalDateTime getExpirationDate() { return expirationDate.get(); }

    /**
     * Gets the expiration date property.
     *
     * @return expiration date property
     */
    public ObjectProperty<LocalDateTime> expirationDateProperty() { return expirationDate; }

    /**
     * Gets the target quantity.
     *
     * @return target quantity
     */
    public double getTarget() { return target.get(); }

    /**
     * Sets the target quantity.
     *
     * @param v new target quantity
     */
    public void setTarget(Double v) { target.set(v); }

    /**
     * Gets the target property.
     *
     * @return target property
     */
    public DoubleProperty targetProperty() { return target; }

    /**
     * Gets the fill ratio property.
     *
     * @return read-only fill ratio property
     */
    public ReadOnlyDoubleProperty fillRatioProperty() { return fillRatio.getReadOnlyProperty(); }

}


