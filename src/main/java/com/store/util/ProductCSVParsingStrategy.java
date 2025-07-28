package com.store.util;

import com.store.model.Product;
import org.apache.commons.csv.CSVRecord;

import java.math.BigDecimal;

public class ProductCSVParsingStrategy implements CSVParsingStrategy {
    private static final String[] EXPECTED_HEADERS = {"name", "description", "company", "price", "stock"};
    
    @Override
    public Product parseRecord(CSVRecord record) throws FileProcessingException {
        try {
            String name = record.get("name");
            String description = record.get("description");
            String company = record.get("company");
            String priceStr = record.get("price");
            String stockStr = record.get("stock");
            
            if (name == null || name.trim().isEmpty()) {
                throw new FileProcessingException("Product name is required");
            }
            
            BigDecimal price;
            try {
                price = new BigDecimal(priceStr);
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    throw new FileProcessingException("Price cannot be negative");
                }
            } catch (NumberFormatException e) {
                throw new FileProcessingException("Invalid price format: " + priceStr);
            }
            
            Integer stock;
            try {
                stock = Integer.parseInt(stockStr);
                if (stock < 0) {
                    throw new FileProcessingException("Stock cannot be negative");
                }
            } catch (NumberFormatException e) {
                throw new FileProcessingException("Invalid stock format: " + stockStr);
            }
            
            return new Product(name.trim(), description != null ? description.trim() : "", 
                             company != null ? company.trim() : "", price, stock);
        } catch (IllegalArgumentException e) {
            throw new FileProcessingException("Missing required CSV columns", e);
        }
    }
    
    @Override
    public String[] getExpectedHeaders() {
        return EXPECTED_HEADERS.clone();
    }
}