package com.example.microservices.userservice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservices.userservice.dao.UserRepository;
import com.example.microservices.userservice.model.User;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Environment environment;
	
	@GetMapping("/users")
	public List<User> retrieveAllUsers() {
		List<User> findAll = userRepository.findAll();
		Integer port = Integer.parseInt(environment.getProperty("local.server.port"));
		findAll.forEach(user -> user.setPort(port));
		return findAll;
	}
	
	@GetMapping("/users/{id}")
	public User retrieveUser(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		Integer port = Integer.parseInt(environment.getProperty("local.server.port"));
		
		if(user.isPresent()) {
			User userObj = user.get();
			userObj.setPort(port);
			return userObj;
		}
		
		return null;
	}
	
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userRepository.deleteById(id);
	}
	
	@PostMapping("/users")
	public void createUser(@RequestBody User user) {
		userRepository.save(user);
	}
	
}
