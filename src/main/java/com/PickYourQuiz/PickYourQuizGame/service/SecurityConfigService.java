package com.PickYourQuiz.PickYourQuizGame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityConfigService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public SecurityConfigService(BCryptPasswordEncoder passwordEncoder) {
		super();
		this.passwordEncoder = passwordEncoder;
	}

	public String hashPassword(String plainTextPassword) {
		String hashedPassword = passwordEncoder.encode(plainTextPassword);
		return hashedPassword;
	} 
	
}
