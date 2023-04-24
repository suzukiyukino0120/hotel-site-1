package com.example.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
	 * プラン・宿泊日程・宿泊人数確認画面
	 * @param date
	 * @param planId
	 * @param roomId
	 * @param model
	 * @return
	 */
	@RequestMapping("/plan")
	public String planView(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, Integer planId, Integer roomId, Model model) {
		ConfirmPlanForm confirmPlanForm =(ConfirmPlanForm) model.getAttribute("confirmPlanForm");
		Plan plan = searchPlanService.searchPlanById(planId);
		confirmPlanForm.setPlanId(planId);
		confirmPlanForm.setCheckinDate(date);
		model.addAttribute("confirmPlanForm", confirmPlanForm);
		session.setAttribute("planId",plan.getPlanId());
		session.setAttribute("planName",plan.getPlanName());//表示用
		session.setAttribute("checkinDate",date);//表示用
		session.setAttribute("guestCapacity", plan.getRoom().getGuestCapacity());//対象の部屋の上限人数内から選択できるようにセレクトボックスを作成
		return "confirm_plan";
	}
	
	/**
	 * 選択したプラン・宿泊日程・宿泊人数を確認する
	 * @param form
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping("/plan/confirm")
	public String confirmPlan(@Validated ConfirmPlanForm confirmPlanForm, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "confirm_plan";
		}
		
		int stayDays = (int) ChronoUnit.DAYS.between(confirmPlanForm.getCheckinDate(),confirmPlanForm.getCheckoutDate());
		Plan plan = searchPlanService.searchVacancyRoomPlan(confirmPlanForm.getPlanId(),confirmPlanForm.getCheckinDate(),confirmPlanForm.getCheckoutDate());
		Integer[] roomsFee = new Integer[stayDays];
		for(int i=0; i<plan.getRoom().getVacancyRoomCalender().size(); i++) {
			for(int j=0; j<roomsFee.length; j++) {
				roomsFee[j]=plan.getRoom().getVacancyRoomCalender().get(i).getRoomFee();
			}
		}
		session.setAttribute("checkoutDate",confirmPlanForm.getCheckoutDate());//表示用
		session.setAttribute("guestNumber",confirmPlanForm.getGuestNumber());//表示用
		session.setAttribute("totalPrice",Reservation.calcTotalPrice(plan.getPlanFee(),roomsFee, stayDays, confirmPlanForm.getGuestNumber()));//表示用
		return "redirect:/reservation/form";
	}
	
	/**
	 * 予約情報を入力するフォームを表示
	 * @return
	 */
	@RequestMapping("/form")
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
	public String reservationCnplete(@Validated ReservationForm reservationForm, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "reservation_confirm";
		}
		reservationForm.setReservationUser(0);//ユーザー機能未実装のためデフォルトで0
		reservationForm.setCreateUser(0);//ユーザー機能未実装のためデフォルトで0
		reservationForm.setUpdateUser(0);//ユーザー機能未実装のためデフォルトで0
		Integer reservationId = reservationService.reservation(reservationForm);
		model.addAttribute("reservationId", reservationId);
		return"reservation_complete";
	}
}
