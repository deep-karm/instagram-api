package com.insta.instagram.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insta.instagram.dto.UserDto;
import com.insta.instagram.exceptions.StoryException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.model.Story;
import com.insta.instagram.model.User;
import com.insta.instagram.repository.StoryRepository;
import com.insta.instagram.repository.UserRepository;
import com.insta.instagram.service.StoryService;
import com.insta.instagram.service.UserService;

@Service
public class StoryServiceImpl implements StoryService{

	@Autowired
	private StoryRepository storyRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Story createStory(Story story, Integer userId) throws UserException {
		User user = userService.findUserById(userId);
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(userId);
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());
		
		story.setUser(userDto);
		story.setTimestamp(LocalDateTime.now());
		
		user.getStories().add(story);
		
		Story createdStory = storyRepository.save(story);
		userRepository.save(user);

		return createdStory;
	}

	@Override
	public List<Story> findStoryByUserId(Integer userId) throws UserException, StoryException {
//		List<Story> stories = storyRepository.findAllStoryByUserId(userId);
		
		User user = userService.findUserById(userId);
		if(user.getStories().size()==0) {
			throw new StoryException("No Stories for userId: " + userId);
		}
		List<Story> stories = user.getStories();
		return stories;
	}

}
