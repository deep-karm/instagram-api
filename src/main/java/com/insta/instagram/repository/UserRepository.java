package com.insta.instagram.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.insta.instagram.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	public Optional<User> findByEmail(String email);
	public Optional<User> findByUsername(String username);
	
	@Query("select u from User u where u.id in :users")
	public List<User> findAllUsersByUserIds(@Param("users") List<Integer> userIds);
	
	@Query("select distinct u from User u "
			+ "where u.username like %:query% "
			+ "or u.email like %:query% "
			+ "or u.name like %:query%")
	public List<User> findByQuery(@Param("query") String query);
}
