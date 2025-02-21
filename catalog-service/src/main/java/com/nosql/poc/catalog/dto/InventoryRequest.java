package com.nosql.poc.catalog.dto;

import com.nosql.poc.catalog.model.Inventory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.util.Map;

@Data
public class InventoryRequest {
    @NotNull(message = "Channel ID is required")
    private String channelId;

    @NotNull(message = "Location ID is required")
    private String locationId;

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity must be zero or positive")
    private Integer quantity;

    private String status;

    private Map<String, Object> inventoryAttributes;

    public Inventory toInventory() {
        Inventory inventory = new Inventory();
        inventory.setChannelId(this.channelId);
        inventory.setLocationId(this.locationId);
        inventory.setQuantity(this.quantity);
        inventory.setStatus(this.status);
        inventory.setInventoryAttributes(this.inventoryAttributes);
        return inventory;
    }
}
