package com.store.dao;

import com.store.model.Login;
import com.store.util.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface LoginDAO {
    Login save(Login login) throws DatabaseException;
    Optional<Login> findById(Integer id) throws DatabaseException;
    Optional<Login> findByEmail(String email) throws DatabaseException;
    List<Login> findAll() throws DatabaseException;
    Login update(Login login) throws DatabaseException;
    boolean delete(Integer id) throws DatabaseException;
    boolean existsByEmail(String email) throws DatabaseException;
}