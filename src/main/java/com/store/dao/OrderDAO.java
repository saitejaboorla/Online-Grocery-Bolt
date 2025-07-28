package com.store.dao;

import com.store.model.Order;
import com.store.util.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface OrderDAO {
    Order save(Order order) throws DatabaseException;
    Optional<Order> findById(Integer id) throws DatabaseException;
    List<Order> findByCustomerId(Integer customerId) throws DatabaseException;
    List<Order> findAll() throws DatabaseException;
    Order update(Order order) throws DatabaseException;
    boolean delete(Integer id) throws DatabaseException;
    Optional<Order> findCartByCustomerId(Integer customerId) throws DatabaseException;
}