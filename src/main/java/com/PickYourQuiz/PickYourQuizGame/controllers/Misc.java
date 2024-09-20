package com.PickYourQuiz.PickYourQuizGame.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Misc {

	@GetMapping("/")
	public String getHomePage() {
		return "FrontPage";
	}
	
	@GetMapping("/rules")
	public String getRulesPage() {
		return "Rules";
	}
	
}
