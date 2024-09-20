package com.PickYourQuiz.PickYourQuizGame.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.SecurityFilterChain;

import com.PickYourQuiz.PickYourQuizGame.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class BasicAuthConfig extends WebSecurityConfiguration {
	
	private CustomUserDetailsService userDetailsService;

	public BasicAuthConfig(CustomUserDetailsService userDetailsService) {
		super();
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/", "/signin", "/signup", "/rules", "/addUser").permitAll()
				.requestMatchers("/static/**", "/images/**", "/Style.css").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin(form -> form
				.loginPage("/signin")
				.loginProcessingUrl("/checkCredentials")
				.failureUrl("/signin?error=true")
				.defaultSuccessUrl("/enterTopic")
			)
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID")
			)
			.userDetailsService(userDetailsService);
		
		return http.build();
	}
	
}
