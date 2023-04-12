package com.example.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.createForm.CreateSearchForm;
import com.example.domain.Code;
import com.example.repository.CodeMstRepository;

@Service
@Transactional
public class CreateFormService {
	@Autowired
	CodeMstRepository codeMstRepository;
	
	/**
	 * プラン検索欄を生成する
	 * @return　プラン検索欄name,value
	 */
	public CreateSearchForm findSearchForm() {
		CreateSearchForm createSearchForm = new CreateSearchForm();
		
		Map<String,String> mealTypeMap = new HashMap<>();
		for(Code code:codeMstRepository.selectByGroupCode("meal_type")) {
			mealTypeMap.put(code.getCodeName(), code.getCode());
		}
		createSearchForm.setMealType(mealTypeMap);
		
		Map<String,String> roomTypeMap = new HashMap<>();
		for(Code code:codeMstRepository.selectByGroupCode("room_type")) {
			roomTypeMap.put(code.getCodeName(), code.getCode());
		}
		createSearchForm.setRoomType(roomTypeMap);
		
		Map<String,String> roomRankMap = new HashMap<>();
		for(Code code:codeMstRepository.selectByGroupCode("room_rank")) {
			roomRankMap.put(code.getCodeName(), code.getCode());
		}
		createSearchForm.setRoomRank(roomRankMap);
		
		Map<String,String> smokingTypeMap = new HashMap<>();
		for(Code code:codeMstRepository.selectByGroupCode("smoking_type")) {
			smokingTypeMap.put(code.getCodeName(), code.getCode());
		}
		createSearchForm.setSmokingType(smokingTypeMap);
		
		Map<String,String> bathroomTypeMap = new HashMap<>();
		for(Code code:codeMstRepository.selectByGroupCode("bathroom_type")) {
			bathroomTypeMap.put(code.getCodeName(), code.getCode());
		}
		createSearchForm.setBathroomType(bathroomTypeMap);
		
		Map<Integer,Integer> guestNumberMap = new HashMap<>();
		for(Code code:codeMstRepository.selectByGroupCode("guest_number")) {
			guestNumberMap.put(Integer.parseInt(code.getCodeName()), Integer.parseInt(code.getCode()));
		}
		createSearchForm.setGuestCapacity(guestNumberMap);
		
		return createSearchForm;
	}
	
}
