package com.store.dto;

public enum Status {
    Active("Active"),
    Inactive("Inactive");
    
    private final String value;
    
    Status(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static Status fromString(String value) {
        for (Status status : Status.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown Status: " + value);
    }
}