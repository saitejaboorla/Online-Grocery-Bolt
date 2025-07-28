package com.store.dto;

public class CustomerRegistrationDTO {
    private String name;
    private String email;
    private String password;
    private String contact;
    private String address;
    
    public CustomerRegistrationDTO() {}
    
    public CustomerRegistrationDTO(String name, String email, String password, String contact, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.contact = contact;
        this.address = address;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    @Override
    public String toString() {
        return "CustomerRegistrationDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", contact='" + contact + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}