package com.PickYourQuiz.PickYourQuizGame.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.PickYourQuiz.PickYourQuizGame.dto.UserDTO;
import com.PickYourQuiz.PickYourQuizGame.exceptions.UserAlreadyExistsException;
import com.PickYourQuiz.PickYourQuizGame.models.User;
import com.PickYourQuiz.PickYourQuizGame.repo.UserRepository;
import com.PickYourQuiz.PickYourQuizGame.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class AccountOperations {

	private final UserService userService;
	private UserRepository userRepository;
	
	@GetMapping("/signin")
	public String showSignInForm(@RequestParam(required = false) String error, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
		if (error != null) {
			model.addAttribute("error", "Incorrect username or password!");
		}
		
		if (userService.userSignedIn(session, redirectAttributes)) {
			return "redirect:/enterTopic";
		} // User already logged in
		
		return "SignIn";
	} 
	
	@GetMapping("/signup")
	public String showSignUpForm(Model model) {
		model.addAttribute("userDTO", new UserDTO());
		return "SignUp";
	}
	
	@GetMapping("/enterTopic")
	public String enterTopicPage(@AuthenticationPrincipal UserDetails userDetails, Model model, HttpSession session) {
		String username = userDetails.getUsername();
		User currentUser = userRepository.findByUsername(username);
		
		userService.setupEnterTopicPage(currentUser, session, model); // add data to models/session
		
		return "EnterTopic";
	}
	
	@PostMapping("/addUser")
	public String addNewUser(@Valid UserDTO user, BindingResult bindingResult, @RequestParam String confirmPassword, 
							Model model, RedirectAttributes redirectAttrs, HttpSession session) {
		
		if (session.getAttribute("userId") != null) {
			return "FrontPage";
		} // User signed in and backs out, return home page
		
		if (bindingResult.hasErrors()) {
			return "SignUp";
		}
		
		if (!user.getPassword().equals(confirmPassword)) {
			model.addAttribute("error", "Passwords do not match!");
			return "SignUp";
		}
		
		try {
			
			userService.createNewUser(user);
			redirectAttrs.addFlashAttribute("success", "Successfully created account, please sign in");
			return "redirect:/signin";
			
		} catch (UserAlreadyExistsException e) {
			model.addAttribute("error", e.getMessage());
			return "SignUp";
		} catch (Exception e) {
			model.addAttribute("error", "There was an error in sign up proccess, try again");
			return "SignUp";
		}
		
	}
	
}
