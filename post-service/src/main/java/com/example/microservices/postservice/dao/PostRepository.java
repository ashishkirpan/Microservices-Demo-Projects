package com.example.microservices.postservice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.microservices.postservice.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

	@Query(value="FROM Post p WHERE p.userId=:userId")
	public List<Post> findByUserId(int userId);
	
}
