package com.PickYourQuiz.PickYourQuizGame.dto;

import com.PickYourQuiz.PickYourQuizGame.models.User;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	
	@Size(min = 10, message = "Username must be atleast 10 characters")
	private String username;
	
	@Size(min = 10, message = "Password must be atleast 10 characters")
	private String password;

	public UserDTO(User user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
	}
	
	public User toEntity() {
		User user = new User();
		user.setPassword(this.password);
		user.setUsername(this.username);
		return user;
	}
	
}
