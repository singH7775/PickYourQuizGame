package com.PickYourQuiz.PickYourQuizGame.models;


import org.hibernate.annotations.ColumnDefault;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users_stats")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Stats {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "current_points", nullable = false)
	@ColumnDefault("50")
	private Long currentPoints = 50L;
	
	@Column(name = "total_wins", nullable = false)
	private int totalWins;
	
	@Column(name = "total_losses", nullable = false)
	private int totalLosses;
	
}
