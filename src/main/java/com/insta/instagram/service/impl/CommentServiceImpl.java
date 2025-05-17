package com.insta.instagram.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insta.instagram.dto.UserDto;
import com.insta.instagram.exceptions.CommentException;
import com.insta.instagram.exceptions.PostException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.model.Comment;
import com.insta.instagram.model.Post;
import com.insta.instagram.model.User;
import com.insta.instagram.repository.CommentRepository;
import com.insta.instagram.repository.PostRepository;
import com.insta.instagram.service.CommentService;
import com.insta.instagram.service.PostService;
import com.insta.instagram.service.UserService;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private PostRepository postRepository;
	
	@Override
	public Comment createComment(Comment comment, Integer postId, Integer userId) throws UserException, PostException {
		User user = userService.findUserById(userId);
		Post post = postService.findPostById(postId);
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(userId);
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());
		
		comment.setUser(userDto);
		comment.setCreatedAt(LocalDateTime.now());
		
		Comment createdComment = commentRepository.save(comment);
		post.getComments().add(createdComment);
		postRepository.save(post);
		
		return createdComment;
	}

	@Override
	public Comment findCommentById(Integer commentId) throws CommentException {
		Optional<Comment> commentOptional = commentRepository.findById(commentId);
		if(commentOptional.isPresent()) {
			return commentOptional.get();
		}
		throw new CommentException("Comment not found for id: "+ commentId);
	}

	@Override
	public Comment likeUnlikeCommentAction(Integer commentId, Integer userId, boolean isLiked)
			throws CommentException, UserException {
		User user = userService.findUserById(userId);
		Comment comment = findCommentById(commentId);
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(userId);
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());
		
		if(isLiked) {
			comment.getLikedByUsers().add(userDto);
		}
		else if(comment.getLikedByUsers().contains(userDto)) {
			comment.getLikedByUsers().remove(userDto);
		}
		comment=commentRepository.save(comment);
		return comment;
	}

}
