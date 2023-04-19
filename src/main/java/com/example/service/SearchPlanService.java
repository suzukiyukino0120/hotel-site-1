package com.example.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Plan;
import com.example.domain.Room;
import com.example.form.SearchPlanForm;
import com.example.repository.PlanMstRepository;
import com.example.repository.RoomMstRepository;

@Service
@Transactional
public class SearchPlanService {
	@Autowired
	PlanMstRepository planMstRepository;
	
	@Autowired
	RoomMstRepository roomMstRepository;
	
	/**
	 * 任意の検索条件を指定してプランを検索
	 * @param form
	 * @return
	 */
	public List<Plan> searchPlan(SearchPlanForm form){
		return planMstRepository.search(form);
	}
	
	/**
	 * プランIDで一件検索
	 * @param planId
	 * @return　plan
	 */
	public Plan searchPlanById(Integer planId) {
		return planMstRepository.selectPlanById(planId);
	}
	
	/**
	 * プランID・日付を指定して空室があるプランを取得
	 * @param planId
	 * @param startDate
	 * @param endDate
	 * @return　plan
	 */
	public Plan searchVacancyRoomPlan(Integer planId, LocalDate startDate, LocalDate endDate) {
		return planMstRepository.selectVacancyRoomPlan(planId, startDate, endDate);
	}
	
}
