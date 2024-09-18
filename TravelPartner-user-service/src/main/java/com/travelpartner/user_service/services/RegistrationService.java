package com.travelpartner.user_service.services;

import org.springframework.http.ResponseEntity;

import com.travelpartner.user_service.entity.UserEntity;

import jakarta.validation.Valid;

public interface RegistrationService {

	ResponseEntity<?> createUserInfo(@Valid UserEntity userEntity);

}
