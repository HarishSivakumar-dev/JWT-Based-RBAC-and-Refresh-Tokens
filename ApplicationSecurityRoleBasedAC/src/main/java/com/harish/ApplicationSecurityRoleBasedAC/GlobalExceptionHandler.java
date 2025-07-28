package com.harish.ApplicationSecurityRoleBasedAC;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.MethodNotAllowed;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler
{

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<?> noHandlerException(NoHandlerFoundException ex)
	{
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				             .body("Error: "+"Method Not Found"+" "+ex.getMessage());
	}
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> badcredentialsException(BadCredentialsException ex)
	{
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
							 .body("Error: "+"Unauthorized !"+" "+ex.getMessage());
	}
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> requestNotSupportedException(HttpRequestMethodNotSupportedException ex)
	{
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
							 .body("Error: "+"Method Not Allowed !"+" "+ex.getMessage());
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> GlobalException(Exception ex)
	{
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							 .body("Error: "+" "+ex.getMessage());
	}
	
}
