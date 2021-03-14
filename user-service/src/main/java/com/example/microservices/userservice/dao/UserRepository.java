package com.example.microservices.userservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.microservices.userservice.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
