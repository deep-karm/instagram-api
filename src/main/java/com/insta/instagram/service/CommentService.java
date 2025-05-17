package com.insta.instagram.service;

import com.insta.instagram.exceptions.CommentException;
import com.insta.instagram.exceptions.PostException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.model.Comment;

public interface CommentService {
	public Comment createComment(Comment comment, Integer postId, Integer userId) throws UserException,PostException;
	public Comment findCommentById(Integer commentId) throws CommentException;
	public Comment likeUnlikeCommentAction(Integer commentid, Integer userId, boolean isliked) throws CommentException,UserException;
	
}
