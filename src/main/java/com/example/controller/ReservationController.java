package com.example.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Plan;
import com.example.form.ConfirmPlanForm;
import com.example.service.SearchPlanService;
import com.fasterxml.jackson.annotation.JsonFormat;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
	@Autowired
	HttpSession session;
	
	@Autowired
	SearchPlanService searchPlanService;
	
	@ModelAttribute
	public ConfirmPlanForm setupConfirmPlanForm() {
		return new ConfirmPlanForm();
	}
	
	@RequestMapping("/")
	public String index(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, Integer planId, Integer roomId, Model model) {
		ConfirmPlanForm confirmPlanForm =(ConfirmPlanForm) model.getAttribute("confirmPlanForm");
		Plan plan = searchPlanService.searchPlanById(planId);
		confirmPlanForm.setPlanId(planId);
		confirmPlanForm.setCheckinDate(date);
		model.addAttribute("confirmPlanForm", confirmPlanForm);
		session.setAttribute("planName",plan.getPlanName());//表示用
		session.setAttribute("guestCapacity", plan.getRoom().getGuestCapacity());//対象の部屋の上限人数内から選択できるようにセレクトボックスを作成
		return "confirm_plan";
	}
	
	@RequestMapping("/confirmPlan")
	public String confirmPlan(@Validated ConfirmPlanForm form, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "confirm_plan";
		}
		return "confirm_plan";
	}
	

}
