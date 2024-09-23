package com.travelpartner.user_service.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travelpartner.user_service.config.CustomResponse;
import com.travelpartner.user_service.dao.RegistrationDAO;
import com.travelpartner.user_service.entity.UserEntity;
import com.travelpartner.user_service.utill.Utills;

import jakarta.validation.Valid;

@Service
public class RegistrationServiceImp implements RegistrationService {

	@Autowired
	RegistrationDAO registrationDAO;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	Utills utill;

	@Override
	public ResponseEntity<?> createUserInfo(@Valid UserEntity userEntity) {
		// TODO Auto-generated method stub
		Optional<UserEntity> existingUser = registrationDAO.isUserExists(userEntity.getEmail());

		if (existingUser.isPresent()) {

			String errorMessages = "User email already exists. Try with a different email.";
			
			CustomResponse<String> responseBody = new CustomResponse<>(errorMessages, "BAD_REQUEST",
					HttpStatus.BAD_REQUEST.value(),"/api/v1/add/user",LocalDateTime.now());
			
			// If the user exists, return a message with a bad status

			return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
		}

		String userName = userEntity.getUserName() != null ? userEntity.getUserName() : null;

		String email = userEntity.getEmail() != null ? userEntity.getEmail() : null;

		String role = "ROLE_USER,";

		String password = userEntity.getPassword() != null ? passwordEncoder.encode(userEntity.getPassword()) : null;

		String uuid = utill.generateString(36);

		LocalDateTime createdAt = LocalDateTime.now();

		String createdBy = uuid;

		UserEntity userDetails = new UserEntity(userName, email, role, password, uuid, createdAt, createdBy);

		UserEntity userInfo = registrationDAO.createUser(userDetails);

		System.out.println("userInfo" + " " + userInfo);
		
		CustomResponse<UserEntity> responseBody = new CustomResponse<>(userInfo, "CREATED",
				HttpStatus.OK.value(),"/api/v1/add/user",LocalDateTime.now());

		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

}
