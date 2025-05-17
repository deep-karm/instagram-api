package com.insta.instagram.controllers.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.model.User;
import com.insta.instagram.repository.UserRepository;
import com.insta.instagram.service.UserService;

@RestController
public class UserLoginController {

	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/signup")
	public ResponseEntity<User> registerUser (@RequestBody User user) throws UserException {
		User newUser = userService.registerUser(user);
		return new ResponseEntity<User>(newUser,HttpStatus.CREATED);
	}
	
	@GetMapping("/signin")
	public ResponseEntity<User> signin(Authentication auth) throws BadCredentialsException {
		
		Optional<User> userOptional = userRepository.findByEmail(auth.getName());
		if(userOptional.isPresent()) {
			return new ResponseEntity<User>(userOptional.get(),HttpStatus.ACCEPTED);
		}
		throw new BadCredentialsException("Invalid username or password.");
	}
}
