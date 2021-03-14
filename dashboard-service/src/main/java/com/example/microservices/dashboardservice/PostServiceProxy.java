package com.example.microservices.dashboardservice;

import java.util.List;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.microservices.dashboardservice.model.Post;

@FeignClient(name="post-service")
@RibbonClient(name="post-service")
public interface PostServiceProxy {

	@GetMapping("/posts")
	public List<Post> retrieveAllPost();
	
}
