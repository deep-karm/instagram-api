package com.insta.instagram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insta.instagram.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{

	@Query("select p from Post p where p.user.id=?1")
	public List<Post> findByUserId(Integer userId);
	
	@Query("select p from Post p where p.user.id in :users order by p.createdAt desc")
	public List<Post> findAllPostsByAllUserIds(@Param("users") List<Integer> userIds);
}
