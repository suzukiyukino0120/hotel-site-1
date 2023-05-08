package com.example.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
	@Autowired 
	private HttpSession session;
	
	@GetMapping("/loginForm")
    String loginForm() {
        return "login";
    }
	
	@GetMapping("/logined")
	public String logined() {
        if(session.getAttribute("planId")!=null) {
        	return"redirect:/reservation/";
        }else {
        	return"redirect:/home";
        }
	}
	
	@PostMapping("/logout")
	public String logout() {
		return "redirect:/home";
	}
}
