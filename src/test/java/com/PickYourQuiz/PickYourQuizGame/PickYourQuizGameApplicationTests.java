package com.PickYourQuiz.PickYourQuizGame;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.PickYourQuiz.PickYourQuizGame.dto.UserDTO;
import com.PickYourQuiz.PickYourQuizGame.exceptions.UserAlreadyExistsException;
import com.PickYourQuiz.PickYourQuizGame.models.Stats;
import com.PickYourQuiz.PickYourQuizGame.models.User;
import com.PickYourQuiz.PickYourQuizGame.repo.UserRepository;
import com.PickYourQuiz.PickYourQuizGame.service.UserService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
class PickYourQuizGameApplicationTests {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserService userService;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	public void UserRepository_SaveUser_ReturnSuccess() {
		// Arrange
		User user = new User(null, "testUser", passwordEncoder.encode("testPassword"), new Stats());
		
		// Act
		User savedUser = userRepository.save(user);
		
		// Assert
		assertThat(savedUser).isNotNull();
		assertThat(savedUser.getId()).isNotNull();
		assertThat(savedUser.getUsername()).isEqualTo("testUser");
		assertThat(passwordEncoder.matches("testPassword", savedUser.getPassword())).isTrue();
		assertThat(savedUser.getStats()).isNotNull();
		
		
		User foundUser = userRepository.findById(user.getId()).orElse(null);
		assertThat(foundUser).isNotNull();
		assertThat(foundUser).isEqualTo(savedUser);
	}
	
	@Test
	public void UserRepository_SaveUsers_ReturnList() {
		// Arrange
		User user1 = new User(null, "user1", passwordEncoder.encode("password1"), new Stats());
		User user2 = new User(null, "user2", passwordEncoder.encode("password2"), new Stats());
		
		// Act
		userRepository.save(user1);
		userRepository.save(user2);
		List<User> users = (List<User>) userRepository.findAll();
		
		// Assert
		assertThat(users).isNotNull();
		assertThat(users.get(0)).isNotNull();
		assertThat(users.get(1)).isNotNull();
		assertThat(users.get(0).getUsername()).isEqualTo("user1");
		assertThat(users.get(1).getUsername()).isEqualTo("user2");
		assertThat(passwordEncoder.matches("password1", users.get(0).getPassword())).isTrue();
		assertThat(passwordEncoder.matches("password2", users.get(1).getPassword())).isTrue();
	}
	
	@Test
	public void UserRepository_UserAlreadyExists_Test() {
		// Arrange
		Stats testStats = new Stats();
		User user = new User(null, "testUser", passwordEncoder.encode("testPassword"), testStats);
		userRepository.save(user);
		
		UserDTO testUser = new UserDTO("testUser", "testPassword");
		
		// Act
		UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> userService.createNewUser(testUser));
		
		// Assert
		assertEquals("The user testUser already exists", exception.getMessage());
	}
	
	@Test
	public void User_AddPoints_DeletePoints_Test() {
		// Arrange
		User user = new User(null, "testUser", passwordEncoder.encode("testPassword"), new Stats());
		
		// Act
		user.getStats().setCurrentPoints(user.getStats().getCurrentPoints() + 10);
		
		// Assert
		assertEquals(60, user.getStats().getCurrentPoints());
		
		// Act
		user.getStats().setCurrentPoints(user.getStats().getCurrentPoints() - 20);
		
		// Assert
		assertEquals(40, user.getStats().getCurrentPoints());
	}


}
