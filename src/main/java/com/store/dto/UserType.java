package com.store.dto;

public enum UserType {
    Customer("Customer"),
    Admin("Admin");
    
    private final String value;
    
    UserType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static UserType fromString(String value) {
        for (UserType type : UserType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown UserType: " + value);
    }
}