package com.PickYourQuiz.PickYourQuizGame.models;

import groovy.transform.builder.Builder;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "username", nullable = false, unique = true, length = 255)
	private String username;
	
	@Column(name = "password", nullable = false, length = 255)
	private String password;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "users_stats")
	private Stats stats;
	
}
