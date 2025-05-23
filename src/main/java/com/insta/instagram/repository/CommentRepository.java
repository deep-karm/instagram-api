package com.insta.instagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insta.instagram.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{
	
}
