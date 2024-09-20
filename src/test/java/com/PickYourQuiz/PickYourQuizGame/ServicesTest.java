package com.PickYourQuiz.PickYourQuizGame;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.PickYourQuiz.PickYourQuizGame.dto.UserDTO;
import com.PickYourQuiz.PickYourQuizGame.models.Stats;
import com.PickYourQuiz.PickYourQuizGame.models.User;
import com.PickYourQuiz.PickYourQuizGame.repo.UserRepository;
import com.PickYourQuiz.PickYourQuizGame.service.ChatModelService;
import com.PickYourQuiz.PickYourQuizGame.service.SecurityConfigService;
import com.PickYourQuiz.PickYourQuizGame.service.UserService;


public class ServicesTest {

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private SecurityConfigService security;
	
	@Mock
    private RedirectAttributes redirectAttributes;
	
	@Mock
	private ChatClient chatClient;
	
	@Mock
	private ChatClient.Builder chatClientBuilder;
	
	@Mock
	private ChatResponse chatResponse;
	
	private ChatModelService chatModelService;
	
	@InjectMocks
	private UserService userService;
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		
		when((chatClientBuilder).build()).thenReturn(chatClient);
		
		chatModelService = new ChatModelService(chatClientBuilder, userRepository);
		
	}
	
	@Test
	public void UserService_CreateSingleUser_ReturnUser() {
		// Arrange
		User user = new User(null, "test", "hashedPassword", new Stats());
		UserDTO testUserDTO = new UserDTO("test", "password");
		
		when(userRepository.findByUsername("test")).thenReturn(null);
		when(security.hashPassword("password")).thenReturn("hashedPassword");
		when(userRepository.save(any(User.class))).thenReturn(user);
		
		
		// Act
		User createdUser = userService.createNewUser(testUserDTO);
		
		// Assert
		assertThat(createdUser).isNotNull();
		assertThat(createdUser.getUsername()).isEqualTo("test");
		assertThat(createdUser.getPassword()).isEqualTo("hashedPassword");
		assertThat(createdUser.getStats()).isNotNull();
	}
	
	@Test
	public void UserService_CreateUsers_ReturnsUsers() {
		// Arrange
		User user = new User(null, "testUser", "testPassword", new Stats());
		User user1 = new User(null, "testUser1", "testPassword1", new Stats());
		User user2 = new User(null, "testUser2", "testPassword2", new Stats());
		List<User> expectedUsers = Arrays.asList(user, user1, user2);
		when(userRepository.findAll()).thenReturn(expectedUsers);
		
		// Act
		List<User> actualUsers = userService.getUsers();
		
		// Assert
		assertThat(actualUsers).hasSize(3);
	    assertThat(actualUsers).isEqualTo(expectedUsers); 
	}
	
	@Test
	public void QuizIsOverTest() {
		// Arrange
		User user = new User(null, "test", "testPassword", new Stats());
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("questionNumber", 10);
		
		// Act
		Boolean checkGameOver = chatModelService.quizOver(user, session, redirectAttributes);
		
		// Assert
		assertThat(checkGameOver).isTrue();
		
	}
	
	@Test
	public void QuizIsNotOverTest() {
		// Arrange
		User user = new User(null, "test", "testPassword", new Stats());
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("questionNumber", 1);
		
		// Act
		Boolean checkGameOver = chatModelService.quizOver(user, session, redirectAttributes);
		
		// Assert
		assertThat(checkGameOver).isFalse();
		
	}
	
	@Test
	public void gameIsWon() {
		// Arrange
		User user = new User(null, "testUser", "testPassword", new Stats());
		user.getStats().setCurrentPoints((long) 90);
		when(userRepository.save(any(User.class))).thenReturn(user);
		
		ChatClient.ChatClientRequest mockRequest = mock(ChatClient.ChatClientRequest.class);
	    ChatClient.ChatClientRequest.CallResponseSpec mockCallResponse = mock(ChatClient.ChatClientRequest.CallResponseSpec.class);
	    
	    when(chatClient.prompt()).thenReturn(mockRequest);
	    when(mockRequest.user(anyString())).thenReturn(mockRequest);
	    when(mockRequest.call()).thenReturn(mockCallResponse);
	    when(mockCallResponse.content()).thenReturn("True");
		
		// Act
		Boolean result = chatModelService.handleAnswer(user, "Any question", 'A', redirectAttributes);
		
		// Assert
		assertThat(result).isTrue();
		assertThat(user.getStats().getTotalWins()).isEqualTo(1);
		assertThat(user.getStats().getTotalLosses()).isEqualTo(0);
		assertThat(user.getStats().getCurrentPoints()).isEqualTo(50);
		
	}
	
	@Test
	public void gameIsLost() {
		// Arrange
		User user = new User(null, "testUser", "testPassword", new Stats());
		user.getStats().setCurrentPoints((long) 20);
		when(userRepository.save(any(User.class))).thenReturn(user);
		
		ChatClient.ChatClientRequest mockRequest = mock(ChatClient.ChatClientRequest.class);
	    ChatClient.ChatClientRequest.CallResponseSpec mockCallResponse = mock(ChatClient.ChatClientRequest.CallResponseSpec.class);
	    
	    when(chatClient.prompt()).thenReturn(mockRequest);
	    when(mockRequest.user(anyString())).thenReturn(mockRequest);
	    when(mockRequest.call()).thenReturn(mockCallResponse);
	    when(mockCallResponse.content()).thenReturn("False");
		
		// Act
		Boolean result = chatModelService.handleAnswer(user, "Any question", 'A', redirectAttributes);
		
		// Assert
		assertThat(result).isTrue();
		assertThat(user.getStats().getTotalWins()).isEqualTo(0);
		assertThat(user.getStats().getTotalLosses()).isEqualTo(1);
		assertThat(user.getStats().getCurrentPoints()).isEqualTo(50);
		
	}
	
}
