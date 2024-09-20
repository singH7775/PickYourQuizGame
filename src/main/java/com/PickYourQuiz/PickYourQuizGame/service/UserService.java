package com.PickYourQuiz.PickYourQuizGame.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.PickYourQuiz.PickYourQuizGame.dto.UserDTO;
import com.PickYourQuiz.PickYourQuizGame.exceptions.UserAlreadyExistsException;
import com.PickYourQuiz.PickYourQuizGame.models.Stats;
import com.PickYourQuiz.PickYourQuizGame.models.User;
import com.PickYourQuiz.PickYourQuizGame.repo.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {
	
	private UserRepository userRepository;
	private SecurityConfigService security;
	
	public User createNewUser(UserDTO userDTO) throws UserAlreadyExistsException {
		
		if (userRepository.findByUsername(userDTO.getUsername()) != null) {
			throw new UserAlreadyExistsException(userDTO.getUsername());
		}
		
		User newUser = new User();
		newUser.setUsername(userDTO.getUsername());
		newUser.setPassword(security.hashPassword(userDTO.getPassword()));
		
		Stats newStats = new Stats();
		newUser.setStats(newStats);
		
		return userRepository.save(newUser);
	}
	
	public User getUser(Long id) {
		return userRepository.findById(id).orElse(null);
	}
	
	public List<User> getUsers() {
		return (List<User>) userRepository.findAll();
	}
	
	public Boolean userSignedIn(HttpSession session, RedirectAttributes redirectAttributes) {
		if (session.getAttribute("userId") != null) {
			Long userId = (Long)session.getAttribute("userId");
			Optional<User> user1 = userRepository.findById(userId);
			User current_user = user1.get();
			
			redirectAttributes.addFlashAttribute("currentPoints", current_user.getStats().getCurrentPoints());
			redirectAttributes.addFlashAttribute("currentWins", current_user.getStats().getTotalWins());
			redirectAttributes.addFlashAttribute("currentLosses", current_user.getStats().getTotalLosses());
			
			return true;
		}
		return false;
	}
	
	public void setupEnterTopicPage(User currentUser, HttpSession session, Model model) {
		session.setAttribute("questionNumber", 0);
		session.setAttribute("userId", currentUser.getId());
		model.addAttribute("currentPoints", currentUser.getStats().getCurrentPoints());
		model.addAttribute("currentWins", currentUser.getStats().getTotalWins());
		model.addAttribute("currentLosses", currentUser.getStats().getTotalLosses());
	}
	
}
