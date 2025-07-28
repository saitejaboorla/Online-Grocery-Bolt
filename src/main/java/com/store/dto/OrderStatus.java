package com.store.dto;

public enum OrderStatus {
    Cart("Cart"),
    Placed("Placed"),
    Processing("Processing"),
    Shipped("Shipped"),
    Delivered("Delivered"),
    Cancelled("Cancelled");
    
    private final String value;
    
    OrderStatus(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static OrderStatus fromString(String value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown OrderStatus: " + value);
    }
}