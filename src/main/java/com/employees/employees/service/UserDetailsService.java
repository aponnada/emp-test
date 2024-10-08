package com.employees.employees.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.employees.employees.model.User;

public interface UserDetailsService {

	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

	User createUser(User user);

}
