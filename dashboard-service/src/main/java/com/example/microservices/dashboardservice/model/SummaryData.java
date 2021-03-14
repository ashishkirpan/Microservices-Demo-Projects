package com.example.microservices.dashboardservice.model;

public class SummaryData {

	private String description;

	private Integer count;

	private User user;
	
	private Integer port;
	
	public SummaryData() {
		super();
	}

	public SummaryData(String description, Integer count) {
		super();
		this.description = description;
		this.count = count;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
