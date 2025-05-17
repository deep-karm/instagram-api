package com.insta.instagram.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.insta.instagram.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<com.insta.instagram.model.User> userOptional = userRepository.findByEmail(username);
		if(userOptional.isPresent()) {
			com.insta.instagram.model.User user = userOptional.get();
			
			List<GrantedAuthority> authorities = new  ArrayList<GrantedAuthority>();
			return new User(user.getEmail(),user.getPassword(),authorities);
		}
		throw new BadCredentialsException("User not found with username: "+ username);
	}

}
