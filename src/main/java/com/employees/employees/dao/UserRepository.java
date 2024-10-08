package com.employees.employees.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employees.employees.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

}
