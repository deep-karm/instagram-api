package com.insta.instagram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insta.instagram.exceptions.CommentException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.model.Comment;
import com.insta.instagram.model.User;
import com.insta.instagram.service.CommentService;
import com.insta.instagram.service.UserService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/createComment/{postId}")
	public ResponseEntity<Comment> createComment (@RequestBody Comment comment, @PathVariable Integer postId, @RequestHeader("Authorization") String token) throws Exception {
		User user = userService.findUserProfile(token);
		Comment createdComment = commentService.createComment(comment, postId, user.getId());
		return new ResponseEntity<Comment>(createdComment,HttpStatus.CREATED);
	}
	
	@GetMapping("/{commentId}")
	public ResponseEntity<Comment> findCommentById (@PathVariable Integer commentId) throws CommentException {
		Comment comment = commentService.findCommentById(commentId);
		return new ResponseEntity<Comment>(comment,HttpStatus.OK);
	}
	
	@PutMapping("/likeUnlike/{commentId}")
	public ResponseEntity<Comment> likeUnlikeCommentAction(@RequestHeader("Authorization") String token, @PathVariable Integer commentId, @RequestParam boolean isLiked) throws UserException, CommentException {
		User user = userService.findUserProfile(token);
		Comment comment = commentService.likeUnlikeCommentAction(commentId, user.getId(), isLiked);
		return new ResponseEntity<Comment>(comment,HttpStatus.OK);
	}
}

