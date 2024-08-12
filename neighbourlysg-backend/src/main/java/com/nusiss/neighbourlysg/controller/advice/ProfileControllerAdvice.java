package com.nusiss.neighbourlysg.controller.advice;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nusiss.neighbourlysg.exception.EmailInUseException;
import com.nusiss.neighbourlysg.exception.ErrorResponse;
import com.nusiss.neighbourlysg.exception.PasswordWrongException;
import com.nusiss.neighbourlysg.exception.UserNotExistedException;


@RestControllerAdvice
public class ProfileControllerAdvice {
	
	@ExceptionHandler(EmailInUseException.class)
	public ResponseEntity<ErrorResponse> handleEmailInUseException(EmailInUseException e){
		ErrorResponse er= new ErrorResponse("Profile-400","Email already in use. Please try again.", new Date());
		return new ResponseEntity<ErrorResponse>(er, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(PasswordWrongException.class)
	public ResponseEntity<ErrorResponse> handlePasswordWrongException(PasswordWrongException e){
		ErrorResponse er= new ErrorResponse("Profile-400","Wrong Password. Please try again.", new Date());
		return new ResponseEntity<ErrorResponse>(er, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UserNotExistedException.class)
	public ResponseEntity<ErrorResponse> handleUserNotExistedException(UserNotExistedException e){
		ErrorResponse er= new ErrorResponse("Profile-404","User is not existed.Please register", new Date());
		return new ResponseEntity<ErrorResponse>(er, HttpStatus.NOT_FOUND);
	}

}
