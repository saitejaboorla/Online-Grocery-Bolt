package com.store.dao;

import com.store.model.Customer;
import com.store.util.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    Customer save(Customer customer) throws DatabaseException;
    Optional<Customer> findById(Integer id) throws DatabaseException;
    Optional<Customer> findByEmail(String email) throws DatabaseException;
    List<Customer> findAll() throws DatabaseException;
    Customer update(Customer customer) throws DatabaseException;
    boolean delete(Integer id) throws DatabaseException;
}