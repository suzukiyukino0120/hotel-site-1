package com.example.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Calender;
import com.example.repository.CalenderTableRepository;

@Service
@Transactional
public class VacancyRoomService {
	@Autowired
	CalenderTableRepository calenderTableRepository;
	
	/**
	 * 1月単位で空室状況を取得する
	 * @param month
	 * @param planId
	 * @return　空室状況
	 */
	public List<Calender> searchCalender(String month, String planId){
		String strStartOfMonth = month +"-01";
		LocalDate startDate = LocalDate.parse(strStartOfMonth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
		List<Calender> vacancyRoomCalender = calenderTableRepository.selectByPlanId(startDate, endDate, Integer.parseInt(planId));
		
		//カレンダー表示するために月初の前の空白を追加
		int beforeBlank= startDate.getDayOfWeek().getValue() - 1;//前月の最終日の曜日
		for(int i =0; i<=beforeBlank; i++) {
			Calender calender = null;
			vacancyRoomCalender.add(0, calender);
		}
		return vacancyRoomCalender;
	}
}
