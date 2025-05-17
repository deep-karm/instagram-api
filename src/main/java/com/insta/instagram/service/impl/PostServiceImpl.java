package com.insta.instagram.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insta.instagram.dto.UserDto;
import com.insta.instagram.exceptions.PostException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.model.Post;
import com.insta.instagram.model.User;
import com.insta.instagram.repository.PostRepository;
import com.insta.instagram.repository.UserRepository;
import com.insta.instagram.service.PostService;
import com.insta.instagram.service.UserService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Post createPost(Post post,Integer userId) throws UserException {
		User user = userService.findUserById(userId);
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(userId);
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());
		
		post.setUser(userDto);
		Post CreatedPost = postRepository.save(post);
		return CreatedPost;
	}

	@Override
	public String deletePost(Integer postId, Integer userId) throws UserException, PostException {
		Post post = findPostById(postId);
		User user = userService.findUserById(userId);
		
		if(post.getUser().getId().equals(user.getId())) {
			postRepository.deleteById(postId);
			return "Post Successfully deleted";
		}
		throw new PostException("You are not authorized to delete this post.");
	}

	@Override
	public List<Post> findPostByUserid(Integer userId) throws UserException {
		
		List<Post> posts = postRepository.findByUserId(userId);
		if(posts.size()==0) {
			throw new UserException("This user does not have any posts.");
		}

		return posts;
	}

	@Override
	public Post findPostById(Integer postId) throws PostException {

		Optional<Post> postOptional = postRepository.findById(postId);
		if(postOptional.isPresent()) {
			return postOptional.get();
		}
		throw new PostException("Post not found with id: " + postId);
	}

	@Override
	public List<Post> findAllPostByAllUserIds(List<Integer> userIds) throws PostException, UserException {
		List<Post> posts = postRepository.findAllPostsByAllUserIds(userIds);
		if(posts.size()==0) {
			throw new PostException("No posts available");
		}
		return posts;
	}

	@Override
	public String savedPostAction(Integer postId, Integer userId, boolean isSaved) throws PostException, UserException {

		User user = userService.findUserById(userId);
		Post post = findPostById(postId);
		if(isSaved) {
			user.getSavedPosts().add(post);
		}
		else if(user.getSavedPosts().contains(post)){
			 user.getSavedPosts().remove(post);
		}
		userRepository.save(user);
		return "Post action Done successfully";
	}

	@Override
	public Post likePostAction(Integer postId, Integer userId, boolean isLiked) throws UserException, PostException {
		Post post = findPostById(postId);
		User user = userService.findUserById(userId);
		
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(userId);
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());
		
		if(isLiked) {
			post.getLikedByUsers().add(userDto);			
		}
		else if(post.getLikedByUsers().contains(userDto)) {
			post.getLikedByUsers().remove(userDto);
		}
		return postRepository.save(post);
	}

}
