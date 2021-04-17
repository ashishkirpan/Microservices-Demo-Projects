package com.example.microservices.adminportal.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.microservices.adminportal.exception.model.EmailExistException;
import com.example.microservices.adminportal.exception.model.NotAnImageException;
import com.example.microservices.adminportal.exception.model.UserNotFoundException;
import com.example.microservices.adminportal.exception.model.UsernameExistException;
import com.example.microservices.adminportal.model.User;

public interface UserService {

	public User register(String firstName, String lastName, String username, String email)
			throws UsernameExistException, EmailExistException;

	public List<User> getUsers();

	User findUserByUsername(String username);

	User findUserByEmail(String email);

	User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked,
			boolean isActive, MultipartFile profileImage) 
			throws UsernameExistException, EmailExistException, IOException, NotAnImageException;

	User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername,
			String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage)
			throws UsernameExistException, EmailExistException, IOException, NotAnImageException;

	void deleteUser(String username);

	void resetPassword(String email) throws UserNotFoundException;

	User updateProfileImage(String username, MultipartFile profileImage) 
			throws UsernameExistException, EmailExistException, IOException, NotAnImageException;

}
