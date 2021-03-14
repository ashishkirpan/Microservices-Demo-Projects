package com.example.microservices.dashboardservice.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.microservices.dashboardservice.PostServiceProxy;
import com.example.microservices.dashboardservice.UserServiceProxy;
import com.example.microservices.dashboardservice.model.Post;
import com.example.microservices.dashboardservice.model.SummaryData;
import com.example.microservices.dashboardservice.model.User;

@RestController
public class DashboardController {

	@Autowired
	private Environment environment;
	
	@Autowired
	private UserServiceProxy userServiceProxy;
	
	@Autowired
	private PostServiceProxy postServiceProxy;
	
	@GetMapping("/summary/user/{userId}")
	public SummaryData retrieveSummaryDataForUsers(@PathVariable int userId) {
				
		SummaryData data = new SummaryData("User", 3);
		data.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
		
		Map<String, Integer> uriVariables = new HashMap<>();
		uriVariables.put("id", userId);
		
		ResponseEntity<User> responseEntity = new RestTemplate().getForEntity("http://localhost:8080/users/{id}", 
				User.class,
				uriVariables);
		
		User user = responseEntity.getBody();
		data.setUser(user);
		
		return data;
	}
	
	@GetMapping("/summary-feign/user/{userId}")
	public SummaryData retrieveSummaryDataForUsersFeign(@PathVariable int userId) {
				
		SummaryData data = new SummaryData("User", 3);
		//data.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
		
		User user = userServiceProxy.retrieveUser(userId);
		data.setUser(user);
		
		return data;
	}
	
	@GetMapping("/summary/all/user/and/post")
	public List<SummaryData> retrieveSummaryDataAllUserAndPost() {
				
		List<SummaryData> arrayList = new ArrayList<>();
		
		List<User> users = userServiceProxy.retrieveAllUsers();
		SummaryData summaryUsers = new SummaryData("Number of User Registered", users.size());
		arrayList.add(summaryUsers);
		
		List<Post> posts = postServiceProxy.retrieveAllPost();
		SummaryData summaryPosts = new SummaryData("Number of Posts Available", posts.size());
		arrayList.add(summaryPosts);
		
		return arrayList;
	}
}
