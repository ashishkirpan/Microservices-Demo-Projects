package com.example.microservices.adminportal.exception.model;

public class UserNotFoundException extends Exception {

	public UserNotFoundException(String message) {
		super(message);
	}
}