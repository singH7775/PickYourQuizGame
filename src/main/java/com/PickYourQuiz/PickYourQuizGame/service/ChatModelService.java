package com.PickYourQuiz.PickYourQuizGame.service;

import org.springframework.ai.chat.client.ChatClient;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.PickYourQuiz.PickYourQuizGame.exceptions.InternalErrorException;
import com.PickYourQuiz.PickYourQuizGame.models.User;
import com.PickYourQuiz.PickYourQuizGame.repo.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class ChatModelService {
	
	private final ChatClient chatClient;
	private UserRepository userRepository;
	
	public ChatModelService(ChatClient.Builder chatClientBuilder, UserRepository userRepository) {
		
		this.chatClient = chatClientBuilder.build();
		this.userRepository = userRepository;
		
	}
	
	
	// Checks if the current quiz is at 10 questions
	public Boolean quizOver(User user, HttpSession session, RedirectAttributes redirectAtt) {
		Integer questionNumber = (Integer)session.getAttribute("questionNumber");
		if (questionNumber != 10) {
			session.setAttribute("questionNumber", questionNumber + 1);
			return false;
		} else {
			session.setAttribute("questionNumber", 0);
			redirectAtt.addFlashAttribute("currentPoints", user.getStats().getCurrentPoints());
			redirectAtt.addFlashAttribute("currentWins", user.getStats().getTotalWins());
			redirectAtt.addFlashAttribute("currentLosses", user.getStats().getTotalLosses());
			return true;
		}
	}
	
	// Calls the prompt and sets up the question page
	public void setupTheQuestionPage(User user, String topic, HttpSession session, Model model) {
		String prompt = String.format("Give me a super random question about %s with 3 options(labeled A,B,C), 2 being wrong and 1 being right, do not display "
				+ "the right answer and only provide the question and nothing else. Don't make the question too easy", topic);
		
		String question = this.chatClient.prompt().user(prompt).call().content();
		question = question.replaceAll("\n", "<br>"); // Look for /n in the AI response, make those <br> , use th:utext for HTML to read as HTML
		
		session.setAttribute("topic", topic); // Store the topic to call ai over and over
		session.setAttribute("current_question", question);
		model.addAttribute("question", question);
		model.addAttribute("currentPoints", user.getStats().getCurrentPoints());
	}
	
	public Boolean handleAnswer(User user, String current_question, char answer, RedirectAttributes redirectAttrs)  throws InternalErrorException {
		Boolean gameWonLost;
		
		String check_answer = current_question + " (My answer : " + answer + " (If this answer is correct only respond with True or else if incorrect only respond with False))";
		String aiResponse = this.chatClient.prompt().user(check_answer).call().content();
		
		Long current_points = user.getStats().getCurrentPoints();
		if (aiResponse.equals("True")) {
			
			gameWonLost = handleTrueAnswer(user, current_points, redirectAttrs);
			
		} else if (aiResponse.equals("False")) {
			
			gameWonLost = handleFalseAnswer(user, current_points, redirectAttrs);
			
		} else {
			
			throw new InternalErrorException("Internal error");
			
		}
		return gameWonLost;
	}
	
	private Boolean handleTrueAnswer(User user, Long current_points, RedirectAttributes redirectAttrs) {
		if (current_points >= 90) {
			user.getStats().setTotalWins(user.getStats().getTotalWins() + 1);
			user.getStats().setCurrentPoints(50L);
			userRepository.save(user);
			redirectAttrs.addFlashAttribute("resultWin", true);
			redirectAttrs.addFlashAttribute("currentWins", user.getStats().getTotalWins());
			redirectAttrs.addFlashAttribute("currentLosses", user.getStats().getTotalLosses());
			return true;
			
		} else {
			user.getStats().setCurrentPoints(current_points + 10);
			redirectAttrs.addFlashAttribute("correct", true);
		}
		
		redirectAttrs.addFlashAttribute("currentPoints", user.getStats().getCurrentPoints());
		return false;
	
	}
	
	private Boolean handleFalseAnswer(User user, Long current_points, RedirectAttributes redirectAttrs) {
		if (current_points <= 20) {
			user.getStats().setTotalLosses(user.getStats().getTotalLosses() + 1);
			user.getStats().setCurrentPoints(50L);
			userRepository.save(user);
			redirectAttrs.addFlashAttribute("resultLoss", true);
			redirectAttrs.addFlashAttribute("currentWins", user.getStats().getTotalWins());
			redirectAttrs.addFlashAttribute("currentLosses", user.getStats().getTotalLosses());
			return true;
		} else {
			user.getStats().setCurrentPoints(current_points - 20);
			redirectAttrs.addFlashAttribute("incorrect", true);
		}
		
		redirectAttrs.addFlashAttribute("currentPoints", user.getStats().getCurrentPoints());
		return false;
	
	}

}
