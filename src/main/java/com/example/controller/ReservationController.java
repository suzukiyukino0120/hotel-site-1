package com.example.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Plan;
import com.example.domain.Reservation;
import com.example.form.ConfirmPlanForm;
import com.example.form.ReservationForm;
import com.example.service.AccountUserDetails;
import com.example.service.ReservationService;
import com.example.service.SearchPlanService;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
	@Autowired
	HttpSession session;
	
	@Autowired
	SearchPlanService searchPlanService;
	
	@Autowired
	ReservationService reservationService;
	
	@ModelAttribute
	public ConfirmPlanForm setupConfirmPlanForm() {
		return new ConfirmPlanForm();
	}
	
	@ModelAttribute
	public ReservationForm setupReservationForm() {
		return new ReservationForm();
	}
	
	/**
	 * 予約情報を入力するフォームを表示
	 * @return
	 */
	@RequestMapping("/")
	@PreAuthorize("hasRole('ROLE_SA')")
	public String reservationView(Model model) {
		ReservationForm reservationForm = (ReservationForm) model.getAttribute("reservationForm");
		reservationForm.setCheckinDate((LocalDate) session.getAttribute("checkinDate"));
		reservationForm.setCheckoutDate((LocalDate) session.getAttribute("checkoutDate"));
		reservationForm.setGuestNumber((Integer) session.getAttribute("guestNumber"));
		reservationForm.setPlanId((Integer) session.getAttribute("planId"));
		reservationForm.setTotalPrice((Integer) session.getAttribute("totalPrice"));
		model.addAttribute("reservationForm", reservationForm);
		return "reservation";
	}
	
	/**
	 * 予約内容を確認する
	 * @return
	 */
	@RequestMapping("/form/confirm")
	@PreAuthorize("hasRole('ROLE_SA')")
	public String reservationConfirm(@Validated ReservationForm reservationForm, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "reservation";
		}
		model.addAttribute("reservationForm", reservationForm);
		return"reservation_confirm";
	}
	
	/**
	 * 予約を確定する
	 * @param reservationForm
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping("/form/complete")
	@PreAuthorize("hasRole('ROLE_SA')")
	public String reservationCnplete(@AuthenticationPrincipal AccountUserDetails accountUserDetails, @Validated ReservationForm reservationForm, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "reservation_confirm";
		}
		reservationForm.setReservationUser(accountUserDetails.getUserSeqNo());
		reservationForm.setCreateUser(accountUserDetails.getUserSeqNo());
		reservationForm.setUpdateUser(accountUserDetails.getUserSeqNo());
		Integer reservationId = reservationService.reservation(reservationForm);
		model.addAttribute("reservationId", reservationId);
		return"reservation_complete";
	}
}

