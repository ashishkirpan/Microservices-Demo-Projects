package com.example.microservices.adminportal.exception.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
import java.util.Objects;

import javax.persistence.NoResultException;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.microservices.adminportal.model.HttpResponse;

@RestControllerAdvice
public class ExceptionHandling implements ErrorController {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private static final String	ACCOUNT_LOCKED = "Your account has been locked. Please contact Administration";
	private static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint. please send a %s request";
	private static final String INTERNAL_SERVER_ERROR_MSG = "An error occured while processing your request";
	private static final String INCORRECT_CREDENTIALS = "Username / Password incorrect. Please try again";
	private static final String ACCOUNT_DISABLED = "Your account has been disabled. If this is as error, please contact administration";
	private static final String ERROR_PROCESSING_FILE = "Error occured while processing file";
	private static final String NOT_ENOUGH_PERMISSION = "You do not have enough permission";
	public static final String ERROR_PATH = "/error";
	
	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<HttpResponse> accountDisabledException() {
		return createHttpResponse(BAD_REQUEST, ACCOUNT_DISABLED);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<HttpResponse> badCredentialsException() {
		return createHttpResponse(BAD_REQUEST, INCORRECT_CREDENTIALS);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<HttpResponse> accessDeniedException() {
		return createHttpResponse(FORBIDDEN, NOT_ENOUGH_PERMISSION);
	}
	
	@ExceptionHandler(LockedException.class)
	public ResponseEntity<HttpResponse> lockedException() {
		return createHttpResponse(UNAUTHORIZED, ACCOUNT_LOCKED);
	}
	
	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException e) {
		return createHttpResponse(UNAUTHORIZED, e.getMessage());
	}
	
	@ExceptionHandler(EmailExistException.class)
	public ResponseEntity<HttpResponse> emailExistException(EmailExistException e) {
		return createHttpResponse(BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(UsernameExistException.class)
	public ResponseEntity<HttpResponse> usernameExistException(UsernameExistException e) {
		return createHttpResponse(BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(EmailNotFoundException.class)
	public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException e) {
		return createHttpResponse(BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<HttpResponse> methodFoundException(HttpRequestMethodNotSupportedException e) {
		HttpMethod supportedMethod = Objects.requireNonNull(e.getSupportedHttpMethods()).iterator().next();
		return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<HttpResponse> internalServerException(Exception e) {
		LOGGER.error(e.getMessage());
		return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
	}
	
	@ExceptionHandler(NoResultException.class)
	public ResponseEntity<HttpResponse> notFoundException(NoResultException e) {
		LOGGER.error(e.getMessage());
		return createHttpResponse(NOT_FOUND, e.getMessage());
	}
	
	@ExceptionHandler(NotAnImageException.class)
	public ResponseEntity<HttpResponse> notAnImageException(NotAnImageException e) {
		LOGGER.error(e.getMessage());
		return createHttpResponse(BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<HttpResponse> iOException(IOException e) {
		LOGGER.error(e.getMessage());
		return createHttpResponse(INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
	
	/*@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<HttpResponse> noHandlerFoundException(NoHandlerFoundException e) {
		LOGGER.error(e.getMessage());
		return createHttpResponse(BAD_REQUEST, "Page not found");
	}*/
	
	private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message){
	
		return new ResponseEntity<HttpResponse>(new HttpResponse(httpStatus.value(), httpStatus, 
				httpStatus.getReasonPhrase().toUpperCase(), 
				message), httpStatus);
	}

	@RequestMapping(ERROR_PATH)
	public ResponseEntity<HttpResponse> noFound404() {
		return createHttpResponse(NOT_FOUND, "No mapping found this URL");
	}
	
	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
	
}
