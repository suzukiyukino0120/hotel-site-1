package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
	
	@RequestMapping("/")
	public String index() {
		return "reservation";
	}

}
