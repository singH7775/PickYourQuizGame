package com.PickYourQuiz.PickYourQuizGame.controllers;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.PickYourQuiz.PickYourQuizGame.exceptions.InternalErrorException;
import com.PickYourQuiz.PickYourQuizGame.models.User;
import com.PickYourQuiz.PickYourQuizGame.repo.UserRepository;
import com.PickYourQuiz.PickYourQuizGame.service.ChatModelService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/chat")
public class QuizOperations {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ChatModelService chatModelService;
	
	@ModelAttribute("user")
	public User getCurrentUser(HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		if (userId == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in");
		}
		
		Optional<User> currentUser = userRepository.findById(userId);
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
		}
		
		return currentUser.get();
	}
	
	@GetMapping("/question")
	public String question(@RequestParam(value = "topic") String topic, RedirectAttributes redirectAtt, HttpSession session, @ModelAttribute("user") User user,
			Model model) {
		
		if (chatModelService.quizOver(user, session, redirectAtt)) {
			return "redirect:/enterTopic";
		}
		
		chatModelService.setupTheQuestionPage(user, topic, session, model);
		
		return "QuestionPage";
		
	}
	
	@PostMapping("/checkanswer")
	public String checkAnswer(@RequestParam char answer, Model model, RedirectAttributes redirectAttrs, HttpSession session, @ModelAttribute("user") User user) throws InternalErrorException {
		
		String current_question = (String)session.getAttribute("current_question");
		if (current_question == null) {
			throw new InternalErrorException("Internal Error");
		}
		
		
		Boolean gameWonLost = chatModelService.handleAnswer(user, current_question, answer, redirectAttrs);
		
		if (gameWonLost) {
			return "redirect:/enterTopic";
		}
		
		userRepository.save(user);
		return "redirect:/chat/resultPage";
		
	}
	
	@GetMapping("/resultPage")
	public String resultPage() {
		return "ResultPage";
	}
	
}
