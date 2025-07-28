package com.store.dao;

import com.store.dao.impl.*;

public class DAOFactory {
    private static DAOFactory instance;
    
    private DAOFactory() {}
    
    public static synchronized DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }
    
    public CustomerDAO getCustomerDAO() {
        return new CustomerDAOImpl();
    }
    
    public LoginDAO getLoginDAO() {
        return new LoginDAOImpl();
    }
    
    public ProductDAO getProductDAO() {
        return new ProductDAOImpl();
    }
    
    public OrderDAO getOrderDAO() {
        return new OrderDAOImpl();
    }
    
    public OrderItemDAO getOrderItemDAO() {
        return new OrderItemDAOImpl();
    }
}