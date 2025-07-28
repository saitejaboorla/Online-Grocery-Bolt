package com.store.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
    private Integer productId;
    private String name;
    private String description;
    private String company;
    private BigDecimal price;
    private Integer stock;
    private LocalDateTime createdDate;
    
    public Product() {}
    
    public Product(String name, String description, String company, BigDecimal price, Integer stock) {
        this.name = name;
        this.description = description;
        this.company = company;
        this.price = price;
        this.stock = stock;
    }
    
    // Getters and Setters
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", company='" + company + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", createdDate=" + createdDate +
                '}';
    }
}