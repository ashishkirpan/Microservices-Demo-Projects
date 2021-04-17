package com.example.microservices.adminportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.example.microservices.adminportal.constant.SecurityConstant.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.websocket.server.PathParam;

import com.example.microservices.adminportal.constant.FileConstant;
import com.example.microservices.adminportal.exception.model.EmailExistException;
import com.example.microservices.adminportal.exception.model.ExceptionHandling;
import com.example.microservices.adminportal.exception.model.NotAnImageException;
import com.example.microservices.adminportal.exception.model.UserNotFoundException;
import com.example.microservices.adminportal.exception.model.UsernameExistException;
import com.example.microservices.adminportal.model.HttpResponse;
import com.example.microservices.adminportal.model.User;
import com.example.microservices.adminportal.model.UserPrincipal;
import com.example.microservices.adminportal.service.UserService;
import com.example.microservices.adminportal.utility.JWTTokenProvider;

@RestController
@RequestMapping(path = {"/", "/user"})
public class UserController extends ExceptionHandling {

	public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";

	public static final String EMAIL_SENT = "An email with new password was sent to : ";

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JWTTokenProvider jwtTokenProvider;
	
	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user) throws UsernameExistException, EmailExistException {
		authenticate(user.getUsername(), user.getPassword());
		User loginUser = userService.findUserByUsername(user.getUsername());
		UserPrincipal userPrincipal = new UserPrincipal(loginUser);
		HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
		return new ResponseEntity<User>(loginUser, jwtHeader, HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) throws UsernameExistException, EmailExistException {
		User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
		return new ResponseEntity<User>(newUser, HttpStatus.OK);
	}
	
	@PostMapping ("/add")
	public ResponseEntity<User> addUser(@RequestParam("firstName") String firstName, 
			@RequestParam("lastName") String lastName, 
			@RequestParam("username") String username, 
			@RequestParam("email") String email, 
			@RequestParam("role") String role,
			@RequestParam(value = "isNonLocked", required = false) String isNonLocked, 
			@RequestParam(value = "isActive", required = false) String isActive, 
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UsernameExistException, EmailExistException, IOException, NotAnImageException {

		User newUser = userService.addNewUser(firstName, lastName, username, email, role, Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
		return new ResponseEntity<User>(newUser, HttpStatus.OK);
	}
	
	@PostMapping ("/update")
	public ResponseEntity<User> udpateUser(@RequestParam("currentUsername") String currentUsername,
			@RequestParam("firstName") String firstName, 
			@RequestParam("lastName") String lastName, 
			@RequestParam("username") String username, 
			@RequestParam("email") String email, 
			@RequestParam("role") String role,
			@RequestParam("isNonLocked") String isNonLocked, 
			@RequestParam("isActive") String isActive, 
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UsernameExistException, EmailExistException, IOException, NotAnImageException {

		User updatedUser = userService.updateUser(currentUsername, firstName, lastName, username, email, role, Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
		return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
	}
	
	@GetMapping("/find/{username}")
	public ResponseEntity<User> getUser(@PathVariable("username") String username) {
		User user = userService.findUserByUsername(username);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@GetMapping("/list")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.getUsers();
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	@GetMapping("/resetPassword/{email}")
	public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) throws UserNotFoundException {
		userService.resetPassword(email);
		return response(HttpStatus.OK, EMAIL_SENT + email);
	}
	
	@DeleteMapping("/delete/{username}")
	@PreAuthorize("hasAuthority('user:delete')")
	public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username) {
		userService.deleteUser(username);
		return response(HttpStatus.OK, USER_DELETED_SUCCESSFULLY);
	}
	
	@PostMapping ("/updateProfileImage")
	public ResponseEntity<User> udpateUser(@RequestParam("username") String username, 
			@RequestParam(value = "profileImage") MultipartFile profileImage) 
					throws UsernameExistException, EmailExistException, IOException, NotAnImageException {

		User user = userService.updateProfileImage(username, profileImage);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@GetMapping(path = "/image/{username}/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getProfileImage(@PathVariable("username") String username,
			@PathVariable("filename") String filename) throws IOException {
		return Files.readAllBytes(Paths.get(FileConstant.USER_FOLDER + username + FileConstant.FORWARD_SLASH + filename));
	}
	
	@GetMapping(path = "/image/profile/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
		URL url = new URL(FileConstant.TEMP_PROFILE_IMAGE_BASE_URL + username);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try (InputStream inputStream = url.openStream()){
			int byteRead;
			byte[] chunk = new byte[1024];
			while((byteRead = inputStream.read(chunk)) > 0) {
				byteArrayOutputStream.write(chunk, 0, byteRead);
			}
		}
		return byteArrayOutputStream.toByteArray();
	}	
	
	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
		return new ResponseEntity<HttpResponse>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message)
				, httpStatus);
	}

	private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));
		return headers;
	}

	private void authenticate(String username, String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		
	}
	
}
