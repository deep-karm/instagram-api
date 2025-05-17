package com.insta.instagram.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insta.instagram.exceptions.PostException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.model.Post;
import com.insta.instagram.model.User;
import com.insta.instagram.response.MessageResponse;
import com.insta.instagram.service.PostService;
import com.insta.instagram.service.UserService;

@RestController
@RequestMapping("/api/posts")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/created")
	public ResponseEntity<Post> createPost(@RequestBody Post post,@RequestHeader("Authorization") String token) throws PostException,UserException{
		User user = userService.findUserProfile(token);
		Post createdPost = postService.createPost(post, user.getId());
		
		return new ResponseEntity<Post>(createdPost,HttpStatus.CREATED);
	}
	
	@GetMapping("/all/{userId}")
	public ResponseEntity<List<Post>> findPostByUserId (@PathVariable Integer userId) throws PostException,UserException {
		List<Post> posts = postService.findPostByUserid(userId);
		return new ResponseEntity<List<Post>>(posts,HttpStatus.OK);
	}
	
	@GetMapping("/following/{userIds}")
	public ResponseEntity<List<Post>> findAllPostByUserId (@PathVariable List<Integer> userIds) throws PostException,UserException {
		List<Post> posts = postService.findAllPostByAllUserIds(userIds);
		return new ResponseEntity<List<Post>>(posts,HttpStatus.OK);
	}
	
	@GetMapping("/{postId}")
	public ResponseEntity<Post> findPostById (@PathVariable Integer postId) throws PostException {
		Post post = postService.findPostById(postId);
		return new ResponseEntity<Post>(post,HttpStatus.OK);
	}
	
	@PutMapping("/like_action/{postId}")
	public ResponseEntity<Post> LikedPostAction(@PathVariable Integer postId, @RequestHeader("Authorization") String token, @RequestParam boolean isLiked) throws PostException,UserException {
		User user = userService.findUserProfile(token);
		Post updatedPost = postService.likePostAction(postId, user.getId(), isLiked);
		return new ResponseEntity<Post>(updatedPost,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{postId}")
	public ResponseEntity<MessageResponse> deletePost (@PathVariable Integer postId,@RequestHeader("Authorization") String token) throws PostException,UserException {
		User user = userService.findUserProfile(token);
		String message = postService.deletePost(postId, user.getId());
		return new ResponseEntity<MessageResponse>(new MessageResponse(message),HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/save_post/{postId}")
	public ResponseEntity<MessageResponse> savedPostAction (@RequestHeader("Authorization") String token, @PathVariable Integer postId, @RequestParam boolean isSaved) throws PostException,UserException {
		User user = userService.findUserProfile(token);
		String message = postService.savedPostAction(postId, user.getId(), isSaved);
		return new ResponseEntity<MessageResponse>(new MessageResponse(message),HttpStatus.ACCEPTED);
	}
}
