package com.insta.instagram.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.insta.instagram.dto.UserDto;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.model.User;
import com.insta.instagram.repository.UserRepository;
import com.insta.instagram.security.JwtTokenClaims;
import com.insta.instagram.security.JwtTokenProvider;
import com.insta.instagram.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Override
	public User registerUser(User user) throws UserException {
		Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
		if(existingUser.isPresent()) {
			throw new UserException("Email is already Registered with Instagram.");
		}
		existingUser = userRepository.findByUsername(user.getUsername());
		if(existingUser.isPresent()) {
			throw new UserException("Username is already taken. Try again");
		}
		if(user.getEmail()==null || user.getPassword()==null || user.getUsername()==null || user.getName()==null) {
			throw new UserException("All fields are required");
		}
		
		User newUser = new User();
		newUser.setEmail(user.getEmail());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setUsername(user.getUsername());
		newUser.setName(user.getName());
		
		return userRepository.save(newUser);
	}

	@Override
	public User findUserById(Integer userId) throws UserException {
		Optional<User> userOptional = userRepository.findById(userId);
		if(userOptional.isPresent()) {
			return userOptional.get();
		}
		throw new UserException("User Does not exist for id: " + userId);
	}

	@Override
	public User findUserProfile(String token) throws UserException {

		token=token.substring(7);
		JwtTokenClaims jwtTokenClaims = jwtTokenProvider.getClaimsFroClaims(token);
		String email = jwtTokenClaims.getUsername();
		Optional<User> userOptional = userRepository.findByEmail(email);
		if(userOptional.isPresent()) {
			return userOptional.get();
		}
		throw new UserException("Invalid Token");
	}

	@Override
	public User findByUsername(String username) throws UserException {
		Optional<User> userOptional = userRepository.findByUsername(username);
		if(userOptional.isEmpty()) {
			throw new UserException("User not found for username: "+ username);
		}
		return userOptional.get();
	}

	@Override
	public String followUser(Integer regUserId, Integer followUserId) throws UserException {
		User regUser = findUserById(regUserId);
		User followUser = findUserById(followUserId);
		
		UserDto  follower = new UserDto();
		follower.setEmail(regUser.getEmail());
		follower.setId(regUserId);
		follower.setName(regUser.getName());
		follower.setUserImage(regUser.getImage());
		follower.setUsername(regUser.getUsername());
		
		UserDto following = new UserDto();
		following.setEmail(followUser.getEmail());
		following.setId(followUserId);
		following.setName(followUser.getName());
		following.setUserImage(followUser.getImage());
		following.setUsername(followUser.getUsername());
		
		regUser.getFollowing().add(following);
		followUser.getFollower().add(follower);
		userRepository.save(regUser);
		userRepository.save(followUser);
		return "You Followed "+ followUser.getName();
	}

	@Override
	public String unfollowUser(Integer regUserId, Integer followUserId) throws UserException {
		User regUser = findUserById(regUserId);
		User followUser = findUserById(followUserId);
		
		UserDto  follower = new UserDto();
		follower.setEmail(regUser.getEmail());
		follower.setId(regUserId);
		follower.setName(regUser.getName());
		follower.setUserImage(regUser.getImage());
		follower.setUsername(regUser.getUsername());
		
		UserDto following = new UserDto();
		following.setEmail(followUser.getEmail());
		following.setId(followUserId);
		following.setName(followUser.getName());
		following.setUserImage(followUser.getImage());
		following.setUsername(followUser.getUsername());
		
		regUser.getFollowing().remove(following);
		followUser.getFollower().remove(follower);
		userRepository.save(regUser);
		userRepository.save(followUser);
		return "You Unfollowed "+ followUser.getName();
	}

	@Override
	public List<User> findUserByIds(List<Integer> userIds) throws UserException {
		List<User> users = userRepository.findAllUsersByUserIds(userIds);
		return users;
	}

	@Override
	public List<User> searchUser(String query) throws UserException {
		List<User> users = userRepository.findByQuery(query);
		if(users.isEmpty()) {
			throw new UserException("Users not found");
		}
		return users;
	}

	@Override
	public User updateUserDetails(User updatedUser, User existingUser) throws UserException {
		
		if (updatedUser.getBio() != null) {
	        existingUser.setBio(updatedUser.getBio());
	    }
	    
	    if (updatedUser.getUsername() != null) {
	        existingUser.setUsername(updatedUser.getUsername());
	    }
	    
	    if (updatedUser.getName() != null) {
	        existingUser.setName(updatedUser.getName());
	    }
	    
	    if (updatedUser.getEmail() != null) {
	        existingUser.setEmail(updatedUser.getEmail());
	    }
	    
	    if (updatedUser.getMobile() != null) {
	        existingUser.setMobile(updatedUser.getMobile());
	    }
	    
	    if (updatedUser.getWebsite() != null) {
	        existingUser.setWebsite(updatedUser.getWebsite());
	    }
	    
	    if (updatedUser.getGender() != null) {
	        existingUser.setGender(updatedUser.getGender());
	    }
	    
	    if (updatedUser.getImage() != null) {
	        existingUser.setImage(updatedUser.getImage());
	    }
	    
	    if (updatedUser.getPassword() != null) {
	        existingUser.setPassword(updatedUser.getPassword());
	    }
	    
	    existingUser = userRepository.save(existingUser);
	    return existingUser;
	}
	
}
