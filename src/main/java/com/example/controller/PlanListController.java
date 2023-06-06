package com.example.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.domain.Calender;
import com.example.domain.Plan;
import com.example.domain.Reservation;
import com.example.form.ConfirmPlanForm;
import com.example.form.SearchPlanForm;
import com.example.service.CreateFormService;
import com.example.service.SearchPlanService;
import com.example.service.VacancyRoomService;

@Controller
@RequestMapping("/plan")
public class PlanListController {
	@ModelAttribute
	public SearchPlanForm searchPlanForm() { 
		return new SearchPlanForm();
	}
	
	@ModelAttribute
	public ConfirmPlanForm setupConfirmPlanForm() {
		return new ConfirmPlanForm();
	}
	
	@Autowired 
	private HttpSession session;

	@Autowired
	private CreateFormService createFormService;
	
	@Autowired
	private SearchPlanService searchPlanService;
	
	@Autowired

	private VacancyRoomService vacancyRoomService;
	/**
	 * プラン一覧を表示する
	 * @return　プラン一覧画面
	 */
	@RequestMapping("/")
	public String planPage() {
		session.setAttribute("form", createFormService.createSearchPlanForm());
		return "plan_list";
	}
	
	/**
	 * プランを検索する
	 * @param form
	 * @return
	 */
	@GetMapping("/search")
	@ResponseBody
	public Map<String,Object> searchPlan(SearchPlanForm form){
		Map <String, Object> map = new HashMap<>();
		map.put("planList", searchPlanService.searchPlan(form));
		return map;
	}
	
	/**
	 * 空室カレンダーを表示する
	 * @param month
	 * @param planId
	 * @return
	 */
	@GetMapping("/vacancyRoom/search")
	@ResponseBody
	public Map<String, List<Calender>> searchVacancyCalender(@RequestParam("month") String month,
            @RequestParam("planId") String planId){
		Map <String, List<Calender>> map = new HashMap<>();
		map.put("calenderList", vacancyRoomService.searchCalender(month, planId));
		return map;
	}
	
	/**
	 * チェックアウト・宿泊人数選択画面
	 * @param date
	 * @param planId
	 * @param roomId
	 * @param model
	 * @return
	 */
	@RequestMapping("/confirm")
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
	@RequestMapping("/confirm/first")
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
		session.setAttribute("checkoutDate",confirmPlanForm.getCheckoutDate());
		session.setAttribute("guestNumber",confirmPlanForm.getGuestNumber());
		session.setAttribute("totalPrice",Reservation.calcTotalPrice(plan.getPlanFee(),roomsFee, stayDays, confirmPlanForm.getGuestNumber()));
		
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		if(authentication == null) {
			return "redirect:/loginForm";//ログインしていない場合はログイン画面に遷移
		}else {
			return "redirect:/reservation/";//予約画面
		}
	}
}
