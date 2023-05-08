package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Code;
import com.example.domain.Plan;
import com.example.domain.Reservation;
import com.example.form.ReservationForm;
import com.example.form.SearchReservationForm;
import com.example.repository.CalenderTableRepository;
import com.example.repository.CodeMstRepository;
import com.example.repository.PlanMstRepository;
import com.example.repository.ReservationMstRepository;

@Service
@Transactional
public class ReservationService {
	@Autowired
	ReservationMstRepository reservationMstRepository;
	
	@Autowired
	PlanMstRepository planMstRepository;
	
	@Autowired
	CalenderTableRepository calenderTableRepository;
	
	@Autowired
	CodeMstRepository codeMstRepository;
	
	/**
	 * 予約処理
	 * @param reservationForm
	 * @return
	 */
	public Integer reservation(ReservationForm reservationForm) {
		Integer roomId = planMstRepository.selectPlanById(reservationForm.getPlanId()).getRoom().getRoomId();
		Integer reservationId = reservationMstRepository.insertReservation(reservationForm);
		calenderTableRepository.updateVacancyRoom(roomId,reservationForm.getCheckinDate(),reservationForm.getCheckoutDate());
		return reservationId;
	}
	
	/**
	 * ステータスの更新
	 * @param reservationId
	 * @param status
	 */
	public void updateStatus(Integer reservationId, String status) {
		reservationMstRepository.updateStatus(reservationId, status);
	}
	
	/**
	 * 予約情報のコード値を日本語名に変換する
	 * @param reservationList
	 * @return
	 */
	public List<Reservation> changeFromCodeToWord(List<Reservation> reservationList){
		List<Code>mealTypeCodes= codeMstRepository.selectByGroupCode("meal_type");
		List<Code>roomTypeCodes= codeMstRepository.selectByGroupCode("room_type");
		List<Code>roomRankCodes= codeMstRepository.selectByGroupCode("room_rank");
		List<Code>smokingTypeCodes= codeMstRepository.selectByGroupCode("smoking_type");
		List<Code>bathroomTypeCodes= codeMstRepository.selectByGroupCode("bathroom_type");
		List<Code>statusCodes= codeMstRepository.selectByGroupCode("status");
		
		for(Reservation reservation:reservationList) {
			for(Code statusCode:statusCodes) {
				if(statusCode.getCode().equals(reservation.getStatus())){
					reservation.setStatus(statusCode.getCodeName());
				}
			}
			
			Plan plan=reservation.getPlan();
			if(plan!=null) {
			for(Code mealTypeCode:mealTypeCodes) {
				if(mealTypeCode.getCode().equals(plan.getMealType())){
					plan.setMealType(mealTypeCode.getCodeName());
				}
			}
			if(plan.getRoom()!=null) {
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
			}
		}
		return reservationList;
	}
	
	/**
	 * 予約番号で検索
	 * @param reservationId
	 * @return
	 */
	public List<Reservation> selectById(Integer reservationId){
		return changeFromCodeToWord(reservationMstRepository.selectById(reservationId));
	}
	
	/**
	 * 予約者で予約履歴検索
	 * @param reservationUser
	 * @return
	 */
	public List<Reservation> searchByReservationUser(Integer reservationUser){
		return changeFromCodeToWord(reservationMstRepository.selectByReservationUser(reservationUser));
	}
	
	/**
	 * 任意条件で予約情報検索
	 * @param searchReservationForm
	 * @return
	 */
	public List<Reservation> searchByArbitrary(SearchReservationForm searchReservationForm){
		return changeFromCodeToWord(reservationMstRepository.selectByArbitrary(searchReservationForm));
	}
	
	/**
	 * 予約番号で詳細情報取得
	 * @return
	 */
	public Reservation searchReservationDetail(Integer reservationId) {
		return reservationMstRepository.selectDetailInfoByReservationId(reservationId);//コード値から日本語に変換していない
	}
}
