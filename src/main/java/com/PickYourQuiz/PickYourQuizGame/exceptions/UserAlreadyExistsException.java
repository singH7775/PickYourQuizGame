package com.PickYourQuiz.PickYourQuizGame.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public UserAlreadyExistsException(String user) {
		super("The user " + user + " already exists");
	}
	
}
