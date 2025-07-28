package com.store.dao;

import com.store.model.OrderItem;
import com.store.util.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface OrderItemDAO {
    OrderItem save(OrderItem orderItem) throws DatabaseException;
    Optional<OrderItem> findById(Integer id) throws DatabaseException;
    List<OrderItem> findByOrderId(Integer orderId) throws DatabaseException;
    OrderItem update(OrderItem orderItem) throws DatabaseException;
    boolean delete(Integer id) throws DatabaseException;
    boolean deleteByOrderId(Integer orderId) throws DatabaseException;
    Optional<OrderItem> findByOrderIdAndProductId(Integer orderId, Integer productId) throws DatabaseException;
}