package com.PickYourQuiz.PickYourQuizGame.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.PickYourQuiz.PickYourQuizGame.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByUsername(String username);
	
}
