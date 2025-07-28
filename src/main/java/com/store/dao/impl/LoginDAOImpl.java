package com.store.dao.impl;

import com.store.config.DerbyConnectionManager;
import com.store.dao.LoginDAO;
import com.store.dto.Status;
import com.store.dto.UserType;
import com.store.model.Login;
import com.store.util.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoginDAOImpl implements LoginDAO {
    private static final Logger logger = LoggerFactory.getLogger(LoginDAOImpl.class);
    private final DerbyConnectionManager connectionManager;
    
    public LoginDAOImpl() {
        this.connectionManager = DerbyConnectionManager.getInstance();
    }
    
    @Override
    public Login save(Login login) throws DatabaseException {
        String sql = "INSERT INTO login (email, password_hash, user_type, status) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, login.getEmail());
            pstmt.setString(2, login.getPasswordHash());
            pstmt.setString(3, login.getUserType().getValue());
            pstmt.setString(4, login.getStatus().getValue());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseException("Creating login failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    login.setLoginId(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseException("Creating login failed, no ID obtained.");
                }
            }
            
            return login;
        } catch (SQLException e) {
            logger.error("Error saving login", e);
            if (e.getSQLState().equals("23505")) { // Unique constraint violation
                throw new DatabaseException("Email already exists: " + login.getEmail());
            }
            throw new DatabaseException("Error saving login: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Login> findById(Integer id) throws DatabaseException {
        String sql = "SELECT * FROM login WHERE login_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToLogin(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            logger.error("Error finding login by ID", e);
            throw new DatabaseException("Error finding login by ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Login> findByEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM login WHERE email = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToLogin(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            logger.error("Error finding login by email", e);
            throw new DatabaseException("Error finding login by email: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Login> findAll() throws DatabaseException {
        String sql = "SELECT * FROM login ORDER BY created_date DESC";
        List<Login> logins = new ArrayList<>();
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                logins.add(mapRowToLogin(rs));
            }
            
            return logins;
        } catch (SQLException e) {
            logger.error("Error finding all logins", e);
            throw new DatabaseException("Error finding all logins: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Login update(Login login) throws DatabaseException {
        String sql = "UPDATE login SET email = ?, password_hash = ?, user_type = ?, status = ? WHERE login_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, login.getEmail());
            pstmt.setString(2, login.getPasswordHash());
            pstmt.setString(3, login.getUserType().getValue());
            pstmt.setString(4, login.getStatus().getValue());
            pstmt.setInt(5, login.getLoginId());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseException("Updating login failed, no rows affected.");
            }
            
            return login;
        } catch (SQLException e) {
            logger.error("Error updating login", e);
            throw new DatabaseException("Error updating login: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(Integer id) throws DatabaseException {
        String sql = "DELETE FROM login WHERE login_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            logger.error("Error deleting login", e);
            throw new DatabaseException("Error deleting login: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsByEmail(String email) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM login WHERE email = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
            return false;
        } catch (SQLException e) {
            logger.error("Error checking if email exists", e);
            throw new DatabaseException("Error checking if email exists: " + e.getMessage(), e);
        }
    }
    
    private Login mapRowToLogin(ResultSet rs) throws SQLException {
        Login login = new Login();
        login.setLoginId(rs.getInt("login_id"));
        login.setEmail(rs.getString("email"));
        login.setPasswordHash(rs.getString("password_hash"));
        login.setUserType(UserType.fromString(rs.getString("user_type")));
        login.setStatus(Status.fromString(rs.getString("status")));
        
        Timestamp timestamp = rs.getTimestamp("created_date");
        if (timestamp != null) {
            login.setCreatedDate(timestamp.toLocalDateTime());
        }
        
        return login;
    }
}