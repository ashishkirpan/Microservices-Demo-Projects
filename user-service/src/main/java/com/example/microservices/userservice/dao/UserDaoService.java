package com.example.microservices.userservice.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.microservices.userservice.model.User;

@Component
public class UserDaoService {

	private static List<User> users = new ArrayList<User>();
	
	static {
		users.add(new User(1, "Ashish", "A@1234"));
		users.add(new User(2, "Rajesh", "R@1234"));
		users.add(new User(3, "Sachin", "S@1234"));
	}
	
	public List<User> findAll() {
		return users;
	}
	
}
