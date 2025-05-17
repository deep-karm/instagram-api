package com.insta.instagram.service;

import java.util.List;

import com.insta.instagram.exceptions.PostException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.model.Post;

public interface PostService {
	public Post createPost(Post post, Integer userId) throws UserException;
	public String deletePost(Integer postId, Integer userId) throws UserException,PostException;
	public List<Post> findPostByUserid(Integer userId) throws UserException;
	public Post findPostById(Integer postId) throws PostException;
	public List<Post> findAllPostByAllUserIds(List<Integer> userIds) throws PostException,UserException;
	public String savedPostAction(Integer postId, Integer userId, boolean isSaved) throws PostException,UserException;
	public Post likePostAction(Integer postId, Integer userId, boolean isLiked) throws UserException,PostException;
}
