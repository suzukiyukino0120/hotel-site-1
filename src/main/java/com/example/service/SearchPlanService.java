package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Plan;
import com.example.form.SearchPlanForm;
import com.example.repository.PlanMstRepository;

@Service
@Transactional
public class SearchPlanService {
	@Autowired
	PlanMstRepository planMstRepository;
	
	public List<Plan> searchPlan(SearchPlanForm form){
		return planMstRepository.search(form);
	}
	
}
