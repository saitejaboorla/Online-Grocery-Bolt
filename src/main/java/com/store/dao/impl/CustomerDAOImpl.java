package com.store.dao.impl;

import com.store.config.DerbyConnectionManager;
import com.store.dao.CustomerDAO;
import com.store.model.Customer;
import com.store.util.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {
    private static final Logger logger = LoggerFactory.getLogger(CustomerDAOImpl.class);
    private final DerbyConnectionManager connectionManager;
    
    public CustomerDAOImpl() {
        this.connectionManager = DerbyConnectionManager.getInstance();
    }
    
    @Override
    public Customer save(Customer customer) throws DatabaseException {
        String sql = "INSERT INTO customer (name, email, contact, address) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getContact());
            pstmt.setString(4, customer.getAddress());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseException("Creating customer failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setId(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseException("Creating customer failed, no ID obtained.");
                }
            }
            
            return customer;
        } catch (SQLException e) {
            logger.error("Error saving customer", e);
            throw new DatabaseException("Error saving customer: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Customer> findById(Integer id) throws DatabaseException {
        String sql = "SELECT * FROM customer WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToCustomer(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            logger.error("Error finding customer by ID", e);
            throw new DatabaseException("Error finding customer by ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Customer> findByEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM customer WHERE email = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToCustomer(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            logger.error("Error finding customer by email", e);
            throw new DatabaseException("Error finding customer by email: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Customer> findAll() throws DatabaseException {
        String sql = "SELECT * FROM customer ORDER BY created_date DESC";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                customers.add(mapRowToCustomer(rs));
            }
            
            return customers;
        } catch (SQLException e) {
            logger.error("Error finding all customers", e);
            throw new DatabaseException("Error finding all customers: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Customer update(Customer customer) throws DatabaseException {
        String sql = "UPDATE customer SET name = ?, email = ?, contact = ?, address = ? WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getContact());
            pstmt.setString(4, customer.getAddress());
            pstmt.setInt(5, customer.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseException("Updating customer failed, no rows affected.");
            }
            
            return customer;
        } catch (SQLException e) {
            logger.error("Error updating customer", e);
            throw new DatabaseException("Error updating customer: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(Integer id) throws DatabaseException {
        String sql = "DELETE FROM customer WHERE id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            logger.error("Error deleting customer", e);
            throw new DatabaseException("Error deleting customer: " + e.getMessage(), e);
        }
    }
    
    private Customer mapRowToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getInt("id"));
        customer.setName(rs.getString("name"));
        customer.setEmail(rs.getString("email"));
        customer.setContact(rs.getString("contact"));
        customer.setAddress(rs.getString("address"));
        
        Timestamp timestamp = rs.getTimestamp("created_date");
        if (timestamp != null) {
            customer.setCreatedDate(timestamp.toLocalDateTime());
        }
        
        return customer;
    }
}