package com.example.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Reservation;
import com.example.form.SearchReservationForm;
import com.example.service.AccountUserDetails;
import com.example.service.CreateFormService;
import com.example.service.ReservationService;

@Controller
@RequestMapping("/reservationInfo")
public class ReservationInfoController {
	@ModelAttribute
	public SearchReservationForm setupSearchReservationForm() {
		return new SearchReservationForm();
	}
	
	@Autowired 
	private HttpSession session;
	
	@Autowired
	CreateFormService createFormService;
	
	@Autowired
	ReservationService reservationService;
	
	@RequestMapping("/")
	@PreAuthorize("hasRole('ROLE_SA')")
	public String reservationInfoView(@AuthenticationPrincipal AccountUserDetails accountUserDetails, Model model) {
		if(accountUserDetails.getUser().getAuthority().equals("S")) {
			session.setAttribute("form", createFormService.createReservationSearchForm());//管理者権限の時だけ検索欄作成
		}else {
			model.addAttribute("reservationList",reservationService.searchByReservationUser(accountUserDetails.getUser().getUserSeqNo()));
		}
		return "reservation_info";
	}
	
	/**
	 * 管理者用　予約情報を検索する
	 * @param searchReservationForm
	 * @return
	 */
	@RequestMapping("/admin/search")
	@PreAuthorize("hasRole('ROLE_S')")
	public String adminSearch(SearchReservationForm searchReservationForm, Model model) {
		model.addAttribute("reservationList", reservationService.searchByArbitrary(searchReservationForm));
		return "reservation_info";
	}
	
	/**
	 * ユーザー用　予約情報を検索する
	 * @param accountUserDetails
	 */
	public void userSearch(AccountUserDetails accountUserDetails) {
		session.setAttribute("reservationList", reservationService.searchByReservationUser(accountUserDetails.getUser().getUserSeqNo()));
	}
	
	/**
	 * 予約詳細の表示
	 * @param reservationId
	 * @return
	 */
	@RequestMapping("/detail")
	@PreAuthorize("hasRole('ROLE_SA')")
	public String detailInfoView(@AuthenticationPrincipal AccountUserDetails accountUserDetails, Integer reservationId, Model model) {
		Reservation reservationDetail=reservationService.searchReservationDetail(reservationId);
		if(accountUserDetails.getUser().getAuthority().equals("S")) {
			if(reservationDetail.getStatus().equals("0")||reservationDetail.getStatus().equals("1")) {
			model.addAttribute("status", createFormService.createStatusForm(reservationDetail.getStatus()));
			}
		}
		List<Reservation> list=new ArrayList<>();
		list.add(reservationDetail);
		model.addAttribute("reservationDetail", reservationService.changeFromCodeToWord(list).get(0));//コード値から日本語に変換してからモデルに入れる
		return "reservation_info :: detail-html";
	}
	
	/**
	 * 状況の更新
	 * @param reservationId
	 * @param status
	 * @param model
	 * @return
	 */
	@RequestMapping("/updateStatus")
	@PreAuthorize("hasRole('ROLE_S')")
	public String updateStatus(Integer reservationId, String status, Model model) {
		reservationService.updateStatus(reservationId, status);
		model.addAttribute("reservationList", reservationService.selectById(reservationId));
		model.addAttribute("statusMsg", "状況を更新しました。（予約番号： "+reservationId+"）");
		return "reservation_info";
	}
}
