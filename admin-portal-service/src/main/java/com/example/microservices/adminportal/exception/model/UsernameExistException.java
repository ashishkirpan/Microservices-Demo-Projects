package com.example.microservices.adminportal.exception.model;

public class UsernameExistException extends Exception {

	public UsernameExistException(String message) {
		super(message);
	}
}