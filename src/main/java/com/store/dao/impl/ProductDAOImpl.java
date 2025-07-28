package com.store.dao.impl;

import com.store.config.DerbyConnectionManager;
import com.store.dao.ProductDAO;
import com.store.model.Product;
import com.store.util.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAOImpl implements ProductDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProductDAOImpl.class);
    private final DerbyConnectionManager connectionManager;
    
    public ProductDAOImpl() {
        this.connectionManager = DerbyConnectionManager.getInstance();
    }
    
    @Override
    public Product save(Product product) throws DatabaseException {
        String sql = "INSERT INTO product (name, description, company, price, stock) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setString(3, product.getCompany());
            pstmt.setBigDecimal(4, product.getPrice());
            pstmt.setInt(5, product.getStock());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseException("Creating product failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setProductId(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseException("Creating product failed, no ID obtained.");
                }
            }
            
            return product;
        } catch (SQLException e) {
            logger.error("Error saving product", e);
            throw new DatabaseException("Error saving product: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Product> findById(Integer id) throws DatabaseException {
        String sql = "SELECT * FROM product WHERE product_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToProduct(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            logger.error("Error finding product by ID", e);
            throw new DatabaseException("Error finding product by ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Product> findAll() throws DatabaseException {
        String sql = "SELECT * FROM product ORDER BY created_date DESC";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                products.add(mapRowToProduct(rs));
            }
            
            return products;
        } catch (SQLException e) {
            logger.error("Error finding all products", e);
            throw new DatabaseException("Error finding all products: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Product> findAll(int page, int pageSize) throws DatabaseException {
        String sql = "SELECT * FROM product ORDER BY created_date DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, (page - 1) * pageSize);
            pstmt.setInt(2, pageSize);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRowToProduct(rs));
                }
            }
            
            return products;
        } catch (SQLException e) {
            logger.error("Error finding products with pagination", e);
            throw new DatabaseException("Error finding products with pagination: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Product update(Product product) throws DatabaseException {
        String sql = "UPDATE product SET name = ?, description = ?, company = ?, price = ?, stock = ? WHERE product_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setString(3, product.getCompany());
            pstmt.setBigDecimal(4, product.getPrice());
            pstmt.setInt(5, product.getStock());
            pstmt.setInt(6, product.getProductId());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseException("Updating product failed, no rows affected.");
            }
            
            return product;
        } catch (SQLException e) {
            logger.error("Error updating product", e);
            throw new DatabaseException("Error updating product: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(Integer id) throws DatabaseException {
        String sql = "DELETE FROM product WHERE product_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            logger.error("Error deleting product", e);
            throw new DatabaseException("Error deleting product: " + e.getMessage(), e);
        }
    }
    
    @Override
    public int getTotalCount() throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM product";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            logger.error("Error getting total product count", e);
            throw new DatabaseException("Error getting total product count: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Product> searchByName(String name) throws DatabaseException {
        String sql = "SELECT * FROM product WHERE UPPER(name) LIKE UPPER(?) ORDER BY name";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + name + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRowToProduct(rs));
                }
            }
            
            return products;
        } catch (SQLException e) {
            logger.error("Error searching products by name", e);
            throw new DatabaseException("Error searching products by name: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean updateStock(Integer productId, Integer newStock) throws DatabaseException {
        String sql = "UPDATE product SET stock = ? WHERE product_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, newStock);
            pstmt.setInt(2, productId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating product stock", e);
            throw new DatabaseException("Error updating product stock: " + e.getMessage(), e);
        }
    }
    
    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setCompany(rs.getString("company"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStock(rs.getInt("stock"));
        
        Timestamp timestamp = rs.getTimestamp("created_date");
        if (timestamp != null) {
            product.setCreatedDate(timestamp.toLocalDateTime());
        }
        
        return product;
    }
}