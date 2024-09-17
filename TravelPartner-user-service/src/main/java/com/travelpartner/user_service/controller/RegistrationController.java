package com.travelpartner.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelpartner.user_service.entity.UserEntity;
import com.travelpartner.user_service.services.RegistrationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class RegistrationController {
	
	@Autowired
	RegistrationService registrationService;

	@PostMapping("/add/user")
	public ResponseEntity<?> createUser(@Valid @RequestBody UserEntity userEntity, BindingResult result) {

		if (result.hasErrors()) {
			// Collecting error messages
			StringBuilder errorMessages = new StringBuilder();
			result.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("; "));
			
			return new ResponseEntity<>(errorMessages.toString(), HttpStatus.BAD_REQUEST);
		}
		
//		ResponseEntity<?> userInfo = registrationService.createUserInfo(userEntity);
//
//		System.out.println("userInfo" + " " + userInfo);
//		return new ResponseEntity<>(userInfo, HttpStatus.OK);
		
		return registrationService.createUserInfo(userEntity);
	}

}
