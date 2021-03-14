package com.example.microservices.postservice.controller;

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

import com.example.microservices.postservice.dao.PostRepository;
import com.example.microservices.postservice.model.Post;

@RestController
public class PostController {

	@Autowired
	private PostRepository postService;
	
	@Autowired
	private Environment environment;
	
	@GetMapping("/posts")
	public List<Post> retrieveAllPost() {
		int port = Integer.parseInt(environment.getProperty("local.server.port"));
		List<Post> posts = postService.findAll();
		posts.forEach(post -> post.setPort(port));
		return posts;
	}
	
	@GetMapping("/posts/{id}")
	public Post retrievePost(@PathVariable int id) {
		Optional<Post> post = postService.findById(id);
		if(post.isPresent()) {
			return post.get();
		}
		
		return null;
	}
	
	@GetMapping("/posts/user/{userId}")
	public List<Post> retrieveUserPosts(@PathVariable int userId) {
		return postService.findByUserId(userId);
	}
	
	@DeleteMapping("/posts")
	public void deletePost(@PathVariable int id) {
		postService.deleteById(id);
	}
	
	@PostMapping("/posts")
	public void createPost(@RequestBody Post post) {
		postService.save(post);
	}
}
