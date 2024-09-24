package com.travelpartner.user_service.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travelpartner.user_service.config.CustomResponse;
import com.travelpartner.user_service.dao.RegistrationDAO;
import com.travelpartner.user_service.dto.UserInfoDTO;
import com.travelpartner.user_service.entity.UserEntity;
import com.travelpartner.user_service.utill.JwtTokenProvider;
import com.travelpartner.user_service.utill.UserInfo;
import com.travelpartner.user_service.utill.Utills;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Service
public class RegistrationServiceImp implements RegistrationService {

	@Autowired
	RegistrationDAO registrationDAO;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	Utills utill;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider jwtTokenProvider; // You can use JWT or sessions for handling authentication tokens

	@Autowired
	UserInfo userInfo;

	@Override
	public ResponseEntity<?> createUserInfo(@Valid UserEntity userEntity, HttpServletRequest req,
			HttpServletResponse res) {
		// TODO Auto-generated method stub
		Optional<UserEntity> existingUser = registrationDAO.isUserExists(userEntity.getEmail());

		if (existingUser.isPresent()) {

			String errorMessages = "User email already exists. Try with a different email.";

			CustomResponse<String> responseBody = new CustomResponse<>(errorMessages, "BAD_REQUEST",
					HttpStatus.BAD_REQUEST.value(), req.getRequestURI(), LocalDateTime.now());

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

		CustomResponse<UserEntity> responseBody = new CustomResponse<>(userInfo, "CREATED", HttpStatus.OK.value(),
				req.getRequestURI(), LocalDateTime.now());

		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> athunticateUser(UserEntity userEntity, HttpServletRequest req, HttpServletResponse res) {
		try {
			UserDetails userDetails = userInfo.loadUserByUsername(userEntity.getEmail());

			System.out.println("userDetails" + " " + userDetails);

			// Load the user by email
			// Manually authenticate using the AuthenticationManager
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userEntity.getEmail(), userEntity.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			// Optionally, generate a JWT token for the user (or manage sessions)
			String token = jwtTokenProvider.generateToken(authentication);

			System.out.println("authentication" + " " + authentication);

			// Prepare user details for the response
			  UserInfoDTO user = (UserInfoDTO) authentication.getPrincipal();
			Map<String, Object> response = new HashMap<>();
			response.put("token", token);
			response.put("id", user.getId()); // Assuming you have a getId() method
			response.put("username", user.getUserName());
			response.put("email", user.getEmail());
			response.put("roles",
					user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

			CustomResponse<?> responseBody = new CustomResponse<>(response, "SUCCESS", HttpStatus.OK.value(),
					req.getRequestURI(), LocalDateTime.now());

			return new ResponseEntity<>(responseBody, HttpStatus.OK);
		} catch (UsernameNotFoundException ex) {
			// Handle case when user is not found
			CustomResponse<String> responseBody = new CustomResponse<>("Invalid email or password", "BAD_CREDENTIALS",
					HttpStatus.UNAUTHORIZED.value(), req.getRequestURI(), LocalDateTime.now());
			return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
		} catch (BadCredentialsException ex) {
			// Handle case when credentials are invalid
			CustomResponse<String> responseBody = new CustomResponse<>("Invalid email or password", "BAD_CREDENTIALS",
					HttpStatus.UNAUTHORIZED.value(), req.getRequestURI(), LocalDateTime.now());
			return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
		} catch (Exception ex) {
			// Handle any other exceptions
			CustomResponse<String> responseBody = new CustomResponse<>("An error occurred", "ERROR",
					HttpStatus.INTERNAL_SERVER_ERROR.value(), req.getRequestURI(), LocalDateTime.now());
			return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
