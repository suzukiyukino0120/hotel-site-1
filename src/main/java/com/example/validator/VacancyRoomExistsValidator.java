package com.example.validator;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.domain.Plan;
import com.example.service.SearchPlanService;

public class VacancyRoomExistsValidator implements ConstraintValidator<VacancyRoomExists, Object>{
	private String id;
	private String startDate;
	private String endDate;
	
	@Autowired
	SearchPlanService searchPlanService;
	
	@Override
    public void initialize(VacancyRoomExists annotation) {
		this.id=annotation.id();
		this.startDate=annotation.startDate();
		this.endDate=annotation.endDate();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
    	BeanWrapper beanWrapper = new BeanWrapperImpl(value);
    	Integer idVal = (Integer) beanWrapper.getPropertyValue(id);
    	LocalDate startDateVal = (LocalDate) beanWrapper.getPropertyValue(startDate);
    	LocalDate endDateVal = (LocalDate) beanWrapper.getPropertyValue(endDate);
    	//nullチェックはフォームクラスのNotNullでチェックする
    	if(idVal==null||startDateVal==null||endDateVal==null) {
    		return true;
    	}else {
	        // 指定された日程に空室があるかチェックする
	        Plan plan = searchPlanService.searchVacancyRoomPlan(idVal,startDateVal,endDateVal);
	        if(plan != null) {
	            return true;
	        } else {
	            return false;
	        }
    	}
    }
}
