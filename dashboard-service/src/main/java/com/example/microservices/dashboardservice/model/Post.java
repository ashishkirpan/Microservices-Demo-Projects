package com.example.microservices.dashboardservice.model;

public class Post {

	private int id;
	
	private int userId;
	
	private String description;

	private int port;
	
	public Post() {
		super();
	}

	public Post(int id, int userId, String description) {
		super();
		this.id = id;
		this.userId = userId;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
}
