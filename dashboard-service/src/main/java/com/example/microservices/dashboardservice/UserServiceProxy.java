package com.example.microservices.dashboardservice;

import java.util.List;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.microservices.dashboardservice.model.User;

//@FeignClient(name="user-service", url="localhost:8080")
@FeignClient(name="user-service")
@RibbonClient(name="user-service")
public interface UserServiceProxy {

	@GetMapping("/users/{id}")
	public User retrieveUser(@PathVariable int id);
	
	@GetMapping("/users")
	public List<User> retrieveAllUsers();
	
}
