package com.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.createForm.PlanSearchContents;
import com.example.createForm.ReservationSearchContents;
import com.example.domain.Code;
import com.example.domain.Plan;
import com.example.domain.Room;
import com.example.repository.CodeMstRepository;
import com.example.repository.PlanMstRepository;
import com.example.repository.RoomMstRepository;

@Service
@Transactional
public class CreateFormService {
	@Autowired
	CodeMstRepository codeMstRepository;
	
	@Autowired
	PlanMstRepository planMstRepository;

	@Autowired
	RoomMstRepository roomMstRepository;
	
	/**
	 * グループコードでコード値を取得する
	 * @param groupCode
	 * @return
	 */
	public List<Code>searchCodebyGroupCode(String groupCode){
		return codeMstRepository.selectByGroupCode(groupCode);
	}
	
	/**
	 * プラン検索欄を生成する
	 * @return　プラン検索欄name,value
	 */
	public PlanSearchContents createSearchPlanForm() {
		PlanSearchContents planSearchContents = new PlanSearchContents();
		planSearchContents.setMealType(searchCodebyGroupCode("meal_type"));
		planSearchContents.setRoomType(searchCodebyGroupCode("room_type"));
		planSearchContents.setRoomRank(searchCodebyGroupCode("room_rank"));
		planSearchContents.setSmokingType(searchCodebyGroupCode("smoking_type"));
		planSearchContents.setBathroomType(searchCodebyGroupCode("bathroom_type"));
		planSearchContents.setGuestCapacity(searchCodebyGroupCode("guest_number"));
		return planSearchContents;
	}
	
	/**
	 * 予約検索欄を生成する
	 * @return
	 */
	public ReservationSearchContents createReservationSearchForm() {
		ReservationSearchContents reservationSearchContents = new ReservationSearchContents();
		reservationSearchContents.setMealType(searchCodebyGroupCode("meal_type"));
		reservationSearchContents.setRoomType(searchCodebyGroupCode("room_type"));
		reservationSearchContents.setRoomRank(searchCodebyGroupCode("room_rank"));
		reservationSearchContents.setSmokingType(searchCodebyGroupCode("smoking_type"));
		reservationSearchContents.setBathroomType(searchCodebyGroupCode("bathroom_type"));
		reservationSearchContents.setStatus(searchCodebyGroupCode("status"));
		reservationSearchContents.setPlanId(planMstRepository.selectAll());
		reservationSearchContents.setRoomId(roomMstRepository.selectAll());
		return reservationSearchContents;
	}
	
	/**
	 * ステータス更新フォームを生成する
	 * @param status
	 * @return
	 */
	public List<Code> createStatusForm(String status){
		List<Code> statusList=searchCodebyGroupCode("status");
		Iterator<Code> it = statusList.iterator();
		while(it.hasNext()){
            Code statusCode = it.next();
			if(status.equals("0")) {
				if(statusCode.getCode().equals("0")||statusCode.getCode().equals("2")) {
					it.remove();
				}
			}
			if(status.equals("1")) {
				if(statusCode.getCode().equals("0")||statusCode.getCode().equals("1")||statusCode.getCode().equals("3")) {
					it.remove();
				}
			}
		}
		return statusList;
	}
	
	
}
