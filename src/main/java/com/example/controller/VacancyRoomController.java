package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.domain.Calender;
import com.example.service.VacancyRoomService;

@Controller
@RequestMapping("/vacancyRoom")
public class VacancyRoomController {

	@Autowired 
	private HttpSession session;
	
	@Autowired
	private VacancyRoomService vacancyRoomService;
	
	@RequestMapping("/")
	public String index() {
		return "modal_vacancy_room";
	}
	
	
	@GetMapping("/test")
	@ResponseBody
	public Map<String,String> test() {
		Map <String, String> map = new HashMap<>();
		map.put("test", "テスト");
		return map;
	}
	
	@GetMapping("/search")
	@ResponseBody
	public Map<String, List<Calender>> search(@RequestParam("month") String month,
            @RequestParam("planId") String planId){
		System.out.println(month);
		System.out.println(planId);
		Map <String, List<Calender>> map = new HashMap<>();
		map.put("calenderList", vacancyRoomService.searchCalender(month, planId));
		return map;
	}
}
