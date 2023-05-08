package com.example.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Code;
import com.example.domain.Plan;
import com.example.domain.Reservation;
import com.example.form.SearchPlanForm;
import com.example.repository.CodeMstRepository;
import com.example.repository.PlanMstRepository;
import com.example.repository.RoomMstRepository;

@Service
@Transactional
public class SearchPlanService {
	@Autowired
	PlanMstRepository planMstRepository;
	
	@Autowired
	RoomMstRepository roomMstRepository;
	
	@Autowired
	CodeMstRepository codeMstRepository;
	
	/**
	 * 宿泊プラン情報のコード値を日本語名に変換する
	 * @param groupCode
	 * @param code
	 * @return
	 */
	public List<Plan> changeFromCodeToword(List<Plan> planList) {
		List<Code>mealTypeCodes= codeMstRepository.selectByGroupCode("meal_type");
		List<Code>roomTypeCodes= codeMstRepository.selectByGroupCode("room_type");
		List<Code>roomRankCodes= codeMstRepository.selectByGroupCode("room_rank");
		List<Code>smokingTypeCodes= codeMstRepository.selectByGroupCode("smoking_type");
		List<Code>bathroomTypeCodes= codeMstRepository.selectByGroupCode("bathroom_type");
		for(Plan plan:planList) {
		for(Code mealTypeCode:mealTypeCodes) {
			if(mealTypeCode.getCode().equals(plan.getMealType())){
				plan.setMealType(mealTypeCode.getCodeName());
			}
		}
		for(Code roomTypeCode:roomTypeCodes) {
			if(roomTypeCode.getCode().equals(plan.getRoom().getRoomType())){
				plan.getRoom().setRoomType(roomTypeCode.getCodeName());
			}
		}	
		for(Code roomRankCode:roomRankCodes) {
			if(roomRankCode.getCode().equals(plan.getRoom().getRoomRank())){
				plan.getRoom().setRoomRank(roomRankCode.getCodeName());
			}
		}	
		for(Code smokingTypeCode:smokingTypeCodes) {
			if(smokingTypeCode.getCode().equals(plan.getRoom().getSmokingType())){
				plan.getRoom().setSmokingType(smokingTypeCode.getCodeName());
			}
		}	
		for(Code bathroomTypeCode:bathroomTypeCodes) {
			if(bathroomTypeCode.getCode().equals(plan.getRoom().getBathroomType())){
				plan.getRoom().setBathroomType(bathroomTypeCode.getCodeName());
			}
		}	
		}
		return planList;
	}
	
	/**
	 * 任意の検索条件を指定してプランを検索
	 * @param form
	 * @return
	 */
	public List<Plan> searchPlan(SearchPlanForm form){
		List<Plan> planList = planMstRepository.search(form);
		if(planList!=null) {
		for(Plan plan:planList) {
			Integer[] roomsFee = new Integer[plan.getRoom().getVacancyRoomCalender().size()];
			for(int i =0;i<roomsFee.length;i++) {
				roomsFee[i]=plan.getRoom().getVacancyRoomCalender().get(i).getRoomFee();
			}
		Integer totalFeePerPerson =	Reservation.calcTotalPrice(plan.getPlanFee(), roomsFee, plan.getRoom().getVacancyRoomCalender().size(), 1);//一人当たりの合計料金
		plan.setTotalFeePerPerson(totalFeePerPerson);
		}

		return changeFromCodeToword(planList);
	}else {
		return null;
	}
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
