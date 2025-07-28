package com.store.dao;

import com.store.model.Product;
import com.store.util.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    Product save(Product product) throws DatabaseException;
    Optional<Product> findById(Integer id) throws DatabaseException;
    List<Product> findAll() throws DatabaseException;
    List<Product> findAll(int page, int pageSize) throws DatabaseException;
    Product update(Product product) throws DatabaseException;
    boolean delete(Integer id) throws DatabaseException;
    int getTotalCount() throws DatabaseException;
    List<Product> searchByName(String name) throws DatabaseException;
    boolean updateStock(Integer productId, Integer newStock) throws DatabaseException;
}