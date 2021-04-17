package com.example.microservices.adminportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.microservices.adminportal.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findUserByUsername(String username);
	
	User findUserByEmail(String email);
	
	User deleteByUserId(String userId);
}
