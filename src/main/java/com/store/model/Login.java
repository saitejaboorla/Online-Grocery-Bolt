package com.store.model;

import java.time.LocalDateTime;

public class Login {
    private Integer loginId;
    private String email;
    private String passwordHash;
    private UserType userType;
    private Status status;
    private LocalDateTime createdDate;
    
    public Login() {}
    
    public Login(String email, String passwordHash, UserType userType, Status status) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.userType = userType;
        this.status = status;
    }
    
    // Getters and Setters
    public Integer getLoginId() { return loginId; }
    public void setLoginId(Integer loginId) { this.loginId = loginId; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    @Override
    public String toString() {
        return "Login{" +
                "loginId=" + loginId +
                ", email='" + email + '\'' +
                ", userType=" + userType +
                ", status=" + status +
                ", createdDate=" + createdDate +
                '}';
    }
}