package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.form.ReservationForm;
import com.example.repository.CalenderTableRepository;
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
	
	public Integer reservation(ReservationForm reservationForm) {
		Integer roomId = planMstRepository.selectPlanById(reservationForm.getPlanId()).getRoom().getRoomId();
		Integer reservationId = reservationMstRepository.insertReservation(reservationForm);
		calenderTableRepository.updateVacancyRoom(roomId,reservationForm.getCheckinDate(),reservationForm.getCheckoutDate());
		return reservationId;
	}
}
